package com.cryptoregistry.client.storage;

import java.io.StringWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.cryptoregistry.CryptoContact;
import com.cryptoregistry.CryptoKey;
import com.cryptoregistry.KeyGenerationAlgorithm;
import com.cryptoregistry.ListData;
import com.cryptoregistry.MapData;
import com.cryptoregistry.c2.key.Curve25519KeyContents;
import com.cryptoregistry.ec.ECKeyContents;
import com.cryptoregistry.formats.JSONFormatter;
import com.cryptoregistry.ntru.NTRUKeyContents;
import com.cryptoregistry.rsa.RSAKeyContents;
import com.cryptoregistry.signature.CryptoSignature;

/**
 * This is for the use-case where we expect multiple results, as for all key materials registered to "Bob Smith"
 * 
 *
 * @author Dave
 *
 */
public class MultiResultCriteria implements Criteria {

	public final Map<MetadataTokens, Object> map;
	public final List<SingleResult> results;
	
	public MultiResultCriteria() {
		map = new HashMap<MetadataTokens, Object>();
		results = new ArrayList<SingleResult>();
	}
	
	/* (non-Javadoc)
	 * @see com.cryptoregistry.client.storage.Criteria#put(com.cryptoregistry.client.storage.MetadataTokens, java.lang.Object)
	 */
	@Override
	public Object put(MetadataTokens key, Object value) {
		return map.put(key, value);
	}
	
	/* (non-Javadoc)
	 * @see com.cryptoregistry.client.storage.Criteria#toJSON()
	 */
	@Override
	public String toJSON() {
		String regHandle = (String) map.get(MetadataTokens.registrationHandle);
		if(regHandle == null) {
			throw new RuntimeException("toJSON() requires a Criteria where the MetadataTokens.registrationHandle is set");
		}
		
		JSONFormatter builder = new JSONFormatter(regHandle);
		Iterator<SingleResult> iter = results.iterator();
		while(iter.hasNext()){
			SingleResult res = iter.next();
			if(res.metadata.isIgnore()) continue;
			if(res.metadata.isKey()) {
				if(res.metadata.isForPublication()){
					CryptoKey key = (CryptoKey)res.result;
					builder.add(key);
				}else{
					CryptoKey key = (CryptoKey)res.result;
					KeyGenerationAlgorithm alg = key.getMetadata().getKeyAlgorithm();
					switch(alg){
						case Symmetric: continue;
						case Curve25519: {
							Curve25519KeyContents contents = (Curve25519KeyContents) key;
							builder.add(contents.cloneForPublication());
							break;
						}
						case EC: {
							ECKeyContents contents = (ECKeyContents) key;
							builder.add(contents.cloneForPublication());
							break;
						}
						case RSA: {
							RSAKeyContents contents = (RSAKeyContents) key;
							builder.add(contents.cloneForPublication());
							break;
						}
						case NTRU: {
							NTRUKeyContents contents = (NTRUKeyContents) key;
							builder.add(contents.forPublication());
							break;
						}
					case DSA:
						// TODO
						break;
					default:
						break;
					}
				}
			}else if(res.metadata.isContact()){
				builder.add((CryptoContact) res.result);
			}else if(res.metadata.isSignature()){
				builder.add((CryptoSignature) res.result);
			}else if(res.metadata.isNamedMap()){
				builder.add((MapData) res.result);
			}else if(res.metadata.isNamedList()){
				builder.add((ListData) res.result);
			}
		}
		
		StringWriter writer = new StringWriter();
		builder.format(writer);
		return writer.toString();
		
	}

}
