package com.cryptoregistry.signature;

import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.UnsupportedEncodingException;
import java.math.BigInteger;
import java.nio.file.DirectoryStream;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.cryptoregistry.formats.Encoding;
import com.cryptoregistry.formats.FormatUtil;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * The Resolver has the job of finding the values of references of the form uuid:tokenName where uuid
 * is an identifier and tokenName is the key for a child key/value pair. The data might be a cryptographic 
 * primitive, some contact info, a message part, etc. 
 * 
 * The default resolver is given a directory containing JSON encoded files to search over. This implementation
 * is trivial and only functions as a way to prove the concept. A real implementation would use some form
 * of database and/or caching, etc.
 * 
 * @author Dave
 *
 */
public class DefaultResolver implements SignatureReferenceResolver {

	String rootPath;
	
	public DefaultResolver(String path) {
		this.rootPath = path;
	}

	@Override
	public void resolve(String ref, ByteArrayOutputStream collector) throws RefNotFoundException {
		fileIter(ref, collector);
	}
	
	@Override
	public void resolve(List<String> refs, ByteArrayOutputStream collector) throws RefNotFoundException {
		
		for(String ref: refs){
			String ref_p = preprocess(ref);
			fileIter(ref_p, collector);
		}
	}
	
	private String uuid;
	
	/**
	 * Normalize the ref. The ref may have the following forms:
	 * 
	 * uuid:tokenName
	 * uuid(-(U|S|P)):tokenName
	 * .tokenName
	 * 
	 * Where the extended form indicates the mode and dot form is an abbreviation of the first case
	 * 
	 * preprocess normalizes the ref to be in all cases of the form uuid:tokenName 
	 * 
	 * @param ref
	 * @return
	 */
	
	public String preprocess(String ref){
		
		if(ref.startsWith(".")){
			// impossible in the full uuid form, so must be abbreviated form
			StringBuffer buf = new StringBuffer();
			buf.append(uuid);
			buf.append(":");
			buf.append(ref.substring(1,ref.length()));
			return buf.toString();
		}
		
		String token = "";
		if(ref.contains(":")){
			String [] parts = ref.split("\\:");
			if(parts[0].endsWith("-P") || parts[0].endsWith("-U") || parts[0].endsWith("-S")){
				uuid = parts[0].substring(0,parts[0].length() -2);
			}
			else uuid = parts[0];
			token = parts[1];
		}
		StringBuffer buf = new StringBuffer(uuid);
		buf.append(":");
		buf.append(token);
		return buf.toString();
		
	}
	
	void fileIter(String ref, ByteArrayOutputStream collector){
		try (DirectoryStream<Path> ds = 
		  Files.newDirectoryStream(FileSystems.getDefault().getPath(rootPath))) {
			for (Path p : ds) {
				if(p.getFileName().toString().endsWith(".json")) {
					// p is a json file
					FileInputStream fIn = new FileInputStream(p.toFile());
					InputStreamReader reader = new InputStreamReader(fIn, "UTF-8");
					Map<String,Object> map = parse(reader);
					search(map, ref, collector, false);
				}
			}
		} catch (IOException e) {
		   e.printStackTrace();
		}
	}
	
	@SuppressWarnings("unchecked")
	String search(Map<String,Object> map, String ref, ByteArrayOutputStream collector, boolean useEncoding){
		String retVal = null;
		String [] parts = ref.split("\\:");
		String uuid = "", tokenName = "";
		if(parts.length==1) {
			tokenName=parts[0];
		} else if(parts.length==2) {
			uuid=parts[0];
			tokenName=parts[1];
		}
		Iterator<String> iter = map.keySet().iterator();
		while(iter.hasNext()){
			String key = iter.next();
			if(key.equals("Keys")){
				Map<String,Object> inner = (Map<String,Object>)map.get(key);
				retVal = search(inner,ref,collector,true);
				break;
			}
			if(key.equals("Contacts")){
				Map<String,Object> inner = (Map<String,Object>)map.get(key);
				retVal = search(inner,ref,collector,false);
				break;
			}
			if(key.equals(uuid)){
				Map<String,Object> inner = (Map<String,Object>)map.get(key);
				retVal = search(inner,ref,collector,useEncoding);
				break;
			}
			if(key.equals(tokenName)){
				
				// if we've descended down the path from Keys, the value is expected to be Encoded
				if(useEncoding){
					retVal = (String) map.get(tokenName);
					String enc = (String)map.get("Encoding");
					if(enc != null) {
						BigInteger bi = FormatUtil.unwrap(Encoding.valueOf(enc), retVal);
						byte [] b = bi.toByteArray();
						collector.write(b, 0, b.length);
					}
					
					// the values in Contacts would not use Encoding, just collect the bytes from UTF-8 Strings
				}else{
					retVal = (String) map.get(tokenName);
					byte[] b;
					try {
						b = retVal.getBytes("UTF-8");
						collector.write(b, 0, b.length);
					} catch (UnsupportedEncodingException e) {}
				}
				break;
			}
		}
		
		return retVal;
	}
	
	@SuppressWarnings("unchecked")
	Map<String,Object> parse(Reader reader){
		ObjectMapper mapper = new ObjectMapper();
		try {
			return (Map<String, Object>) mapper.readValue(reader, Map.class);
		} catch (Exception x) {
			throw new RuntimeException(x);
		}
	}
	
}
