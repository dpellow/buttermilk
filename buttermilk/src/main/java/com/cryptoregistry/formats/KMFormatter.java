package com.cryptoregistry.formats;

import java.io.IOException;
import java.io.Writer;
import java.util.ArrayList;
import java.util.List;

import com.cryptoregistry.CryptoKeyMetadata;
import com.cryptoregistry.CryptoContact;
import com.cryptoregistry.Version;
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
public class KMFormatter {

	protected String version;
	protected String registrationHandle;
	protected List<CryptoKeyMetadata> keys;
	protected List<CryptoContact> contacts;
	protected List<CryptoSignature> signatures;
	
	public KMFormatter(String handle) {
		version = Version.VERSION;
		this.registrationHandle = handle;
		keys = new ArrayList<CryptoKeyMetadata>();
		contacts = new ArrayList<CryptoContact>();
		signatures = new ArrayList<CryptoSignature>();
	}

	public KMFormatter(String version, String registrationHandle,
			List<CryptoKeyMetadata> keys, List<CryptoContact> contacts,
			List<CryptoSignature> signatures) {
		super();
		this.version = version;
		this.registrationHandle = registrationHandle;
		this.keys = keys;
		this.contacts = contacts;
		this.signatures = signatures;
	}
	
	public void format(Writer writer) {
		JsonFactory f = new JsonFactory();
		JsonGenerator g = null;
		try {
			g = f.createGenerator(writer);
			g.useDefaultPrettyPrinter();
			
			
			
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
