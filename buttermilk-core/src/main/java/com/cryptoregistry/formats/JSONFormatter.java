/*
 *  This file is part of Buttermilk
 *  Copyright 2011-2014 David R. Smith All Rights Reserved.
 *
 */
package com.cryptoregistry.formats;

import java.io.IOException;
import java.io.Writer;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import com.cryptoregistry.CryptoKey;
import com.cryptoregistry.CryptoKeyMetadata;
import com.cryptoregistry.CryptoContact;
import com.cryptoregistry.KeyGenerationAlgorithm;
import com.cryptoregistry.MapData;
import com.cryptoregistry.ListData;
import com.cryptoregistry.Version;
import com.cryptoregistry.c2.key.Curve25519KeyForPublication;
import com.cryptoregistry.ec.ECKeyForPublication;
import com.cryptoregistry.ntru.NTRUKeyForPublication;
import com.cryptoregistry.rsa.RSAKeyForPublication;
import com.cryptoregistry.signature.CryptoSignature;
import com.cryptoregistry.symmetric.SymmetricKeyContents;
import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonGenerator;

/**
 * <pre>
 * Builder which will generate the canonical data structure for a buttermilk format JSON wrapper
 * 
 * It has the general form:
 * 
 * Version
 * Registration Handle
 * Assertions
 *   uuid
 *     IdentityPoints=100
 *     AssertedBy=registrant
 *     Details=URL
 *     "AssertedOn" : "2014-06-22T03:14:50+0000",
      "SignedWith" : "CR1.RSA.SHA-256",
      "s" : "fr_YnzycCZbrlTDbtdjjkqpzSrh7Mjdvmmd1WvBeQnZ980g2e2SSaE7NqzAmcnRVTrGBWb4CIpJpAmEW8PSmIDxBmY7H8-3k5xXvhpW4TpaWK529rrBZ6HGNHndokmwmbVG8Z2etir4UITYMNgpMjbI8l5TDoIe_gpL5vWrYnKLn5Dl2Nuhw7UtyCe1VwCFKcKkmtMzloNnoa9HffxCneYBOGMlyK734VulT--kRV3yjwLinBhrtMfbiwDdCkC-UGmQO-TpPCAbBLPc4sIA7vZEEo-M-zcszWSFDMBXy0YHJ8hghQdZajv-m58-F18e0-wnNLAjAYvEtnsgFHJUANA==",
      "DataRefs" : [ "uuid:IdentityPoints", ".AssertedBy", ".AssertedOn"]
   
 *     
 *     
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
 *</pre>
 *    
 * @author Dave
 *
 */
public class JSONFormatter {

	protected String version;
	protected String registrationHandle;
	protected List<CryptoKey> keys;
	protected List<CryptoContact> contacts;
	protected List<CryptoSignature> signatures;
	protected List<MapData> mapData;
	protected List<ListData> listData;
	
	public JSONFormatter() {
		this("");
	}
	
	public JSONFormatter(String handle) {
		version = Version.OVERALL_VERSION;
		this.registrationHandle = handle;
		keys = new ArrayList<CryptoKey>();
		contacts = new ArrayList<CryptoContact>();
		signatures = new ArrayList<CryptoSignature>();
		mapData = new ArrayList<MapData>();
		listData = new ArrayList<ListData>();
	}

	public JSONFormatter(String version, String registrationHandle,
			List<CryptoKey> keys, List<CryptoContact> contacts,
			List<CryptoSignature> signatures, List<MapData> mapData, 
			List<ListData> listData) {
		super();
		this.version = version;
		this.registrationHandle = registrationHandle;
		this.keys = keys;
		this.contacts = contacts;
		this.signatures = signatures;
		this.mapData = mapData;
		this.listData = listData;
	}
	
	public JSONFormatter add(CryptoContact e) {
		 contacts.add(e);
		 return this;
	}

	public JSONFormatter addContacts(Collection<? extends CryptoContact> c) {
		contacts.addAll(c);
		return this;
	}
	
	public JSONFormatter add(CryptoKey e) {
		keys.add(e);
		return this;
	}
	
	public JSONFormatter addKey(CryptoKey e) {
		keys.add(e);
		return this;
	}

	public JSONFormatter addKeys(Collection<? extends CryptoKey> c) {
		keys.addAll(c);
		return this;
	}
	
	public JSONFormatter add(CryptoSignature e) {
		signatures.add(e);
		return this;
	}

	public JSONFormatter addSignatures(Collection<? extends CryptoSignature> c) {
		signatures.addAll(c);
		return this;
	}
	
	public JSONFormatter add(MapData e) {
		mapData.add(e);
		return this;
	}

	public JSONFormatter addLocalData(Collection<? extends MapData> c) {
		mapData.addAll(c);
		return this;
	}
	
	public JSONFormatter add(ListData e) {
		listData.add(e);
		return this;
	}

	public JSONFormatter addRemoteData(Collection<? extends ListData> c) {
		listData.addAll(c);
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
			
			if(mapData.size()>0 || listData.size()>0){
				
				g.writeObjectFieldStart("Data");
				
				if(mapData.size()>0){
					
					g.writeObjectFieldStart("Local");
					
						MapDataFormatter ldf = new MapDataFormatter(mapData);
						ldf.format(g, writer);
					
					g.writeEndObject();
				}
				
				
				if(listData.size()>0){
					
					g.writeArrayFieldStart("Remote");
					
						ListDataFormatter rdf = new ListDataFormatter(listData);
						rdf.format(g, writer);
				
					g.writeEndArray();
				}
				
				g.writeEndObject();
				
			}
			
			if(keys.size()> 0) {
				
				g.writeObjectFieldStart("Keys");
				
				// TODO allow public keys to work
				for(CryptoKey key: keys){
					final CryptoKeyMetadata meta = key.getMetadata();
					final KeyGenerationAlgorithm alg = meta.getKeyAlgorithm();
					switch(alg){
						case Symmetric: {
							SymmetricKeyContents contents = (SymmetricKeyContents)key;
							SymmetricKeyFormatter formatter = new SymmetricKeyFormatter(contents);
							formatter.formatKeys(g, writer);
							break;
						}
						case Curve25519: {
							Curve25519KeyForPublication contents = (Curve25519KeyForPublication)key;
							C2KeyFormatter formatter = new C2KeyFormatter(contents);
							formatter.formatKeys(g, writer);
							break;
						}
						case EC: {
							ECKeyForPublication contents = (ECKeyForPublication)key;
							ECKeyFormatter formatter = new ECKeyFormatter(contents);
							formatter.formatKeys(g, writer);
							break;
						}
						case NTRU: {
							NTRUKeyForPublication contents = (NTRUKeyForPublication)key;
							NTRUKeyFormatter formatter = new NTRUKeyFormatter(contents);
							formatter.formatKeys(g, writer);
							break;
						}
						case RSA: {
							RSAKeyForPublication contents = (RSAKeyForPublication)key;
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

	public void setRegistrationHandle(String registrationHandle) {
		this.registrationHandle = registrationHandle;
	}

}
