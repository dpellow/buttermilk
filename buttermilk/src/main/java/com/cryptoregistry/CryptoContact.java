package com.cryptoregistry;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.UUID;

public class CryptoContact {

	protected final Map<String,String> map;
	public final String handle;
	
	public CryptoContact() {
		map = new LinkedHashMap<String,String>();
		handle = UUID.randomUUID().toString();
		map.put("Handle",handle);
	}
	
	public CryptoContact(String handle) {
		map = new LinkedHashMap<String,String>();
		this.handle = handle;
		map.put("Handle",handle);
		
	}
	
	public Map<String,String> add(String key, String value){
		map.put(key,value);
		return map;
	}
	
	public String get(String key){
		return map.get(key);
	}

	public Map<String, String> getMap() {
		return map;
	}

	public String getHandle() {
		return handle;
	}

}
