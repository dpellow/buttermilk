package com.cryptoregistry.client.storage;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MultiResultCriteria {

	public final Map<MetadataTokens, Object> map;
	public final List<SingleResult> results;
	
	public MultiResultCriteria() {
		map = new HashMap<MetadataTokens, Object>();
		results = new ArrayList<SingleResult>();
	}
	
	public Object put(MetadataTokens key, Object value) {
		return map.put(key, value);
	}

}
