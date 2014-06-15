package com.cryptoregistry.formats;

import java.io.IOException;
import java.io.Writer;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import com.cryptoregistry.CryptoKeyMetadata;
import com.cryptoregistry.CryptoContact;
import com.cryptoregistry.KeyGenerationAlgorithm;
import com.cryptoregistry.LocalData;
import com.cryptoregistry.RemoteData;
import com.cryptoregistry.Version;
import com.cryptoregistry.c2.key.Curve25519KeyContents;
import com.cryptoregistry.ec.ECKeyContents;
import com.cryptoregistry.rsa.RSAKeyContents;
import com.cryptoregistry.signature.CryptoSignature;
import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonGenerator;

/**
 * Builder which will generate the canonical data structure for a key materials wrapper
 * 
 * It has the general form:
 * 
 * Version
 * Registration Handle
 * Data
 *  \
 *  local
 *     uuid0
 *       key=value
 *     uuid1
 *       key=value
 *     remote
 *     [
 *       url
 *       url
 *       url
 *    ]
 * Keys
 *   \
 *   key-uuid0
 *    key0 attributes
 *   key-uuid1
 *    key1 attributes
 *    
 * Contacts
 *   \
 *   contact-uuid0
 *      contact info0
 *   contact-uuid1
 *      contact info1
 *      
 * Signatures 
 *   \
 *   signature-uuid0
 *     signature info0 
 *     data-refs [ref0, ref1, ref2, ref3 ]
 *    
 * @author Dave
 *
 */
public class JSONBuilder {

	protected String version;
	protected String registrationHandle;
	protected List<CryptoKeyMetadata> keys;
	protected List<CryptoContact> contacts;
	protected List<CryptoSignature> signatures;
	protected List<LocalData> localData;
	protected List<RemoteData> remoteData;
	
	public JSONBuilder(String handle) {
		version = Version.OVERALL_VERSION;
		this.registrationHandle = handle;
		keys = new ArrayList<CryptoKeyMetadata>();
		contacts = new ArrayList<CryptoContact>();
		signatures = new ArrayList<CryptoSignature>();
		localData = new ArrayList<LocalData>();
		remoteData = new ArrayList<RemoteData>();
	}

	public JSONBuilder(String version, String registrationHandle,
			List<CryptoKeyMetadata> keys, List<CryptoContact> contacts,
			List<CryptoSignature> signatures, List<LocalData> localData, 
			List<RemoteData> remoteData) {
		super();
		this.version = version;
		this.registrationHandle = registrationHandle;
		this.keys = keys;
		this.contacts = contacts;
		this.signatures = signatures;
		this.localData = localData;
		this.remoteData = remoteData;
	}
	
	public JSONBuilder add(CryptoContact e) {
		 contacts.add(e);
		 return this;
	}

	public JSONBuilder addContacts(Collection<? extends CryptoContact> c) {
		contacts.addAll(c);
		return this;
	}
	
	public JSONBuilder add(CryptoKeyMetadata e) {
		keys.add(e);
		return this;
	}

	public JSONBuilder addKeys(Collection<? extends CryptoKeyMetadata> c) {
		keys.addAll(c);
		return this;
	}
	
	public JSONBuilder add(CryptoSignature e) {
		signatures.add(e);
		return this;
	}

	public JSONBuilder addSignatures(Collection<? extends CryptoSignature> c) {
		signatures.addAll(c);
		return this;
	}
	
	public JSONBuilder add(LocalData e) {
		localData.add(e);
		return this;
	}

	public JSONBuilder addLocalData(Collection<? extends LocalData> c) {
		localData.addAll(c);
		return this;
	}
	
	public JSONBuilder add(RemoteData e) {
		remoteData.add(e);
		return this;
	}

	public JSONBuilder addRemoteData(Collection<? extends RemoteData> c) {
		remoteData.addAll(c);
		return this;
	}

	public void format(Writer writer){
		format(writer, true);
	}
	
	public void format(Writer writer, boolean prettyPrint) {
		JsonFactory f = new JsonFactory();
		JsonGenerator g = null;
		try {
			g = f.createGenerator(writer);
			if(prettyPrint)g.useDefaultPrettyPrinter();
			
			g.writeStartObject();
			g.writeStringField("Version", Version.OVERALL_VERSION);
			g.writeStringField("RegHandle", registrationHandle);
			
			if(contacts.size()> 0) {
				
				g.writeObjectFieldStart("Contacts");
				
				ContactFormatter cf = new ContactFormatter(contacts);
				cf.format(g, writer);
				
				g.writeEndObject();
			}
			
			if(localData.size()>0 || remoteData.size()>0){
				
				g.writeObjectFieldStart("Data");
				
				if(localData.size()>0){
					
					g.writeObjectFieldStart("Local");
					
						LocalDataFormatter ldf = new LocalDataFormatter(localData);
						ldf.format(g, writer);
					
					g.writeEndObject();
				}
				
				
				if(remoteData.size()>0){
					
					g.writeArrayFieldStart("Remote");
					
						RemoteDataFormatter rdf = new RemoteDataFormatter(remoteData);
						rdf.format(g, writer);
				
					g.writeEndArray();
				}
				
				g.writeEndObject();
				
			}
			
			if(keys.size()> 0) {
				
				g.writeObjectFieldStart("Keys");
				
				for(CryptoKeyMetadata key: keys){
					final KeyGenerationAlgorithm alg = key.getKeyAlgorithm();
					switch(alg){
						case Curve25519: {
							Curve25519KeyContents contents = (Curve25519KeyContents)key;
							C2KeyFormatter formatter = new C2KeyFormatter(contents);
							formatter.formatKeys(g, writer);
							break;
						}
						case EC: {
							ECKeyContents contents = (ECKeyContents)key;
							ECKeyFormatter formatter = new ECKeyFormatter(contents);
							formatter.formatKeys(g, writer);
							break;
						}
						case RSA: {
							RSAKeyContents contents = (RSAKeyContents)key;
							RSAKeyFormatter formatter = new RSAKeyFormatter(contents);
							formatter.formatKeys(g, writer);
							break;
						}
						default: throw new RuntimeException("alg not recognized: "+alg);
					}
				}
				
				g.writeEndObject();
			}
			
			
			
			if(signatures.size()> 0) {
				
				g.writeObjectFieldStart("Signatures");
				
				SignatureFormatter sf = new SignatureFormatter(signatures);
				sf.format(g, writer);
				
				g.writeEndObject();
			}
			
		} catch (IOException x) {
			throw new RuntimeException(x);
		} finally {
			try {
				if (g != null)
					g.close();
			} catch (IOException e) {
				throw new RuntimeException(e);
			}
		}
	}

}
