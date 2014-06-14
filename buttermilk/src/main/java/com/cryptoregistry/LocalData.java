package com.cryptoregistry;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.UUID;

public class LocalData {

	public final String uuid;
	public final Map<String,String> data;
	
	public LocalData() {
		uuid = UUID.randomUUID().toString();
		data = new LinkedHashMap<String,String>();
	}
	
	public LocalData(String uuid) {
		this.uuid = uuid;
		data = new LinkedHashMap<String,String>();
	}
	
	public void put(String key, String value){
		data.put(key,value);
	}

}
