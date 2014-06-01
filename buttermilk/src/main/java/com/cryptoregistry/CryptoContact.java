package com.cryptoregistry;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.UUID;

public class CryptoContact {

	Map<String,String> map;
	
	public CryptoContact() {
		map = new LinkedHashMap<String,String>();
		map.put("Handle", UUID.randomUUID().toString());
	}
	
	public CryptoContact(String handle) {
		map = new LinkedHashMap<String,String>();
		map.put("Handle", handle);
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

}
