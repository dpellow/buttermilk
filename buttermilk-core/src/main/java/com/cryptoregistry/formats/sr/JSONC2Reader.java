/*
 *  This file is part of Buttermilk
 *  Copyright 2011-2014 David R. Smith All Rights Reserved.
 *
 */
package com.cryptoregistry.formats.sr;

import java.io.File;
import java.io.Reader;
import java.util.LinkedHashMap;
import java.util.Map;

import com.cryptoregistry.c2.key.Curve25519KeyForPublication;
import com.cryptoregistry.passwords.Password;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * <p>
 * JSONReader's design works on the premise the reader instance knows nothing about the key
 * materials it is reading; the file could comtain one key or 100 and a lot of other stuff
 * besides. But sometimes you just need a very simple reader which assumes the 
 * program knows at design time the nature of the key to be read - i.e., the programmer knows
 * what kind of keys she is using and has the ability to rely on what they will look like. 
 * </p>
 * 
 * <p>
 * While less general, this approach allows the programmer to avoid the process of testing 
 * the runtime object to find the type which was parsed using the "KeyMaterials" semantics. 
 * In other words, it is a simple, direct approach.  
 * </p>
 * 
 * <p>This class assumes you are reading a very simple file essentially like the below. It can read
 * all three modes, unsecured, secured, and for publication. To read a secured mode you must have
 * provided the password.
 * </p>
 * 
 * <pre>
 {
  "Version" : "Buttermilk Key Materials 1.0",
  "RegHandle" : "Chinese Eyes",
  "Keys" : {
    "ec3bfc9d-1a6b-4c0e-a668-058a20d5ad4f-U" : {
      "KeyAlgorithm" : "Curve25519",
      "CreatedOn" : "2014-07-31T09:59:27+0000",
      "Encoding" : "Base64url",
      "P" : "tEHb0XrJUzRTQAwHVJgQpO2WQAp0B7JDyhICC39mUHM=",
      "s" : "qi3cgft8KPxH1znyHFkadYnRzV71FO9LFO7Zk4WsPQI=",
      "k" : "WAClm1S_AsU3mmKUxbhfcpJS3PnGKzEhM0YZgsU4QHs="
    }
  }
}
 </pre>
 * 
 * @author Dave
 * @see JSONFormatter
 * 
 */
public class JSONC2Reader {
	
	protected final ObjectMapper mapper;
	protected final Map<String,Object> map;
	protected final Password password; // optional, set if you know the file to be read is in mode S. 
	
	/**
	 * Fails immediately if anything goes wrong reading the file or parsing
	 * @param path
	 */
	@SuppressWarnings("unchecked")
	public JSONC2Reader(File path) {
		
		password = null;
		mapper = new ObjectMapper();
		try {
			map = mapper.readValue(path, Map.class);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
	
	@SuppressWarnings("unchecked")
	public JSONC2Reader(Reader in) {
		
		password = null;
		mapper = new ObjectMapper();
		try {
			map = mapper.readValue(in, Map.class);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
	
	/**
	 * Use when we expect the file is secured (i.e., the key uses mode S).
	 * 
	 * @param file
	 * @param password
	 */
	@SuppressWarnings("unchecked")
	public JSONC2Reader(File path, Password password){

		this.password = password;
		mapper = new ObjectMapper();
		try {
			map = mapper.readValue(path, Map.class);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
	
	/**
	 * Use when we expect the file is secured (i.e., the key uses mode S).
	 * 
	 * @param file
	 * @param password
	 */
	@SuppressWarnings("unchecked")
	public JSONC2Reader(Reader in, Password password) {
		
		this.password = password;
		mapper = new ObjectMapper();
		try {
			map = mapper.readValue(in, Map.class);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	public Curve25519KeyForPublication parse() {

		if(password != null) {
			Map<String,Object> unwrapped = unwrap();
		}else{
			
		}
		return null;
	}
	
	public Map<String,Object> unwrap() {
		Map<String,Object> m = new LinkedHashMap<String,Object>();
		
		return m;
	}
	

}
