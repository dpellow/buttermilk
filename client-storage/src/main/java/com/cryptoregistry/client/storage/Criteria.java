package com.cryptoregistry.client.storage;

public interface Criteria {

	/**
	 * Add criteria items. The value must be of the correct type as expected by the MetadataToken key, 
	 * for example type Boolean for MetadataTokens.forPublication, but a java.util.Date for MetadataTokens.createdOn.
	 * 
	 * @param key
	 * @param value
	 * @return
	 */
	public Object put(MetadataTokens key, Object value);

	/**
	 * Generate a representation of the result in JSON encoding. 
	 * 
	 * @return
	 */
	public String toJSON();

}