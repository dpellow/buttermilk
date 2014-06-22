package com.cryptoregistry;

import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.UUID;

public class CryptoContact {

	protected final Map<String,String> map;
	public final String handle;
	
	public CryptoContact() {
		map = new LinkedHashMap<String,String>();
		handle = UUID.randomUUID().toString();
	}
	
	public CryptoContact(String handle) {
		map = new LinkedHashMap<String,String>();
		this.handle = handle;
	}
	
	public CryptoContact(String handle, Map<String,Object> contents) {
		map = new LinkedHashMap<String,String>();
		this.handle = handle;
		Iterator<String> iter = contents.keySet().iterator();
		while(iter.hasNext()){
			String key = iter.next();
			map.put(key, String.valueOf(map.get(key)));
		}
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
