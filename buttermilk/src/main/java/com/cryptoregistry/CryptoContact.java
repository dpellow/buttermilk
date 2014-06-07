package com.cryptoregistry;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.UUID;

public class CryptoContact {

	private Map<String,String> map;
	
	public CryptoContact() {
		map = new LinkedHashMap<String,String>();
		map.put("Handle", UUID.randomUUID().toString());
	}
	
	public CryptoContact(String handle) {
		map = new LinkedHashMap<String,String>();
		map.put("Handle", handle);
	}
	
	public Map<String,String> add(String key, String value){
		map.put(key,value);
		return map;
	}
	
	public String get(String key){
		return map.get(key);
	}
	
	public void addName(String name){
		map.put("Name", name);
	}
	
	public void addRole(String role){
		map.put("Role", role);
	}
	
	public void addEmail(String email){
		map.put("Email", email);
	}
	
	public void addPhone(String phone){
		map.put("Phone", phone);
	}

	public Map<String, String> getMap() {
		return map;
	}

}
