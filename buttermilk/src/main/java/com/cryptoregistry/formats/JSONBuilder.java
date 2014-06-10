package com.cryptoregistry.formats;

import java.io.IOException;
import java.io.Writer;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import com.cryptoregistry.CryptoKeyMetadata;
import com.cryptoregistry.CryptoContact;
import com.cryptoregistry.KeyGenerationAlgorithm;
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
	
	public JSONBuilder(String handle) {
		version = Version.VERSION;
		this.registrationHandle = handle;
		keys = new ArrayList<CryptoKeyMetadata>();
		contacts = new ArrayList<CryptoContact>();
		signatures = new ArrayList<CryptoSignature>();
	}

	public JSONBuilder(String version, String registrationHandle,
			List<CryptoKeyMetadata> keys, List<CryptoContact> contacts,
			List<CryptoSignature> signatures) {
		super();
		this.version = version;
		this.registrationHandle = registrationHandle;
		this.keys = keys;
		this.contacts = contacts;
		this.signatures = signatures;
	}
	
	public boolean add(CryptoContact e) {
		return contacts.add(e);
	}

	public boolean addContacts(Collection<? extends CryptoContact> c) {
		return contacts.addAll(c);
	}
	
	public boolean add(CryptoKeyMetadata e) {
		return keys.add(e);
	}

	public boolean addKeys(Collection<? extends CryptoKeyMetadata> c) {
		return keys.addAll(c);
	}
	
	public boolean add(CryptoSignature e) {
		return signatures.add(e);
	}

	public boolean addSignatures(Collection<? extends CryptoSignature> c) {
		return signatures.addAll(c);
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
			g.writeStringField("Version", Version.VERSION);
			g.writeStringField("RegHandle", registrationHandle);
			
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
			
			if(contacts.size()> 0) {
				
				g.writeObjectFieldStart("Contacts");
				
				ContactFormatter cf = new ContactFormatter(contacts);
				cf.format(g, writer);
				
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
