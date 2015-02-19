/*
 *  This file is part of Buttermilk
 *  Copyright 2011-2014 David R. Smith All Rights Reserved.
 *
 */
package com.cryptoregistry;

import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.UUID;

/**
 * Backing for a contact record. 
 * 
 * @author Dave
 *
 */
public class CryptoContact {

	public static final int MAX_KEY_LENGTH = 64;
	public static final int MAX_VALUE_LENGTH = 255;
	
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
			String value = map.get(key);
			if(key.length() > MAX_KEY_LENGTH) throw new RuntimeException("key too long");
			if(value.length() > MAX_VALUE_LENGTH) throw new RuntimeException("value too long");
			map.put(key, value);
		}
	}
	
	public Map<String,String> add(String key, String value){
		if(key.length() > MAX_KEY_LENGTH) throw new RuntimeException("key too long");
		if(value.length() > MAX_VALUE_LENGTH) throw new RuntimeException("value too long");
		map.put(key,value);
		return map;
	}
	
	public String get(String key){
		return map.get(key);
	}

	//public Map<String, String> getMap() {
	//	return map;
	//}

	public String getHandle() {
		return handle;
	}

}
