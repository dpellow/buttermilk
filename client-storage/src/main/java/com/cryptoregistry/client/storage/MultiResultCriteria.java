package com.cryptoregistry.client.storage;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
		
		return null;
	}

}
