package com.cryptoregistry;

import java.util.List;

/**
 * Builder which will generate the canonical data structure for a key materials wrapper
 * 
 * It has the general form:
 * 
 * Version
 * Registration Handle
 * CreatedOn
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
public class KeyMaterialsBuilder {

	List<ButtermilkKey> keys;
	List<Contact> contacts;
	
	public KeyMaterialsBuilder() {
		
	}

}
