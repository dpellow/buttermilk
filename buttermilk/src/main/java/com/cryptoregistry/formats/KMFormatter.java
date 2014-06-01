package com.cryptoregistry.formats;

import java.util.ArrayList;
import java.util.List;

import com.cryptoregistry.CryptoKey;
import com.cryptoregistry.CryptoContact;
import com.cryptoregistry.Version;
import com.cryptoregistry.signature.CryptoSignature;

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

	String version;
	String registrationHandle;
	List<CryptoKey> keys;
	List<CryptoContact> contacts;
	List<CryptoSignature> signatures;
	
	public KMFormatter(String handle) {
		version = Version.VERSION;
		this.registrationHandle = handle;
		keys = new ArrayList<CryptoKey>();
		contacts = new ArrayList<CryptoContact>();
		signatures = new ArrayList<CryptoSignature>();
	}

	public KMFormatter(String version, String registrationHandle,
			List<CryptoKey> keys, List<CryptoContact> contacts,
			List<CryptoSignature> signatures) {
		super();
		this.version = version;
		this.registrationHandle = registrationHandle;
		this.keys = keys;
		this.contacts = contacts;
		this.signatures = signatures;
	}
	
	public void format() {
		
	}

}
