/*
 *  This file is part of Buttermilk
 *  Copyright 2011-2014 David R. Smith All Rights Reserved.
 *
 */
package com.cryptoregistry.formats.sr;

import java.io.File;
import java.io.Reader;
import java.util.Map;

import com.cryptoregistry.CryptoKeyWrapper;
import com.cryptoregistry.CryptoKeyWrapperImpl;
import com.cryptoregistry.c2.key.*;
import com.cryptoregistry.formats.C2KeyFormatReader;
import com.cryptoregistry.passwords.Password;
import com.cryptoregistry.pbe.ArmoredPBEResult;
import com.cryptoregistry.pbe.PBEAlg;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * <p>
 * JSONReader's design works on the premise the reader instance knows nothing about the key
 * materials it is reading; the file could contain 1 key or 100 and a lot of other stuff
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
 * 
 * Unsecured mode:
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

Secured Mode:

{
  "Version" : "Buttermilk Key Materials 1.0",
  "RegHandle" : "Chinese Knees",
  "Keys" : {
    "c7a12163-e184-4ea2-bdc1-9a3a7c0e1f7f-S" : {
      "KeyData.Type" : "Curve25519",
      "KeyData.PBEAlgorithm" : "PBKDF2",
      "KeyData.EncryptedData" : "yHF-iiXfM7mNf3jIt7fVir2Vi1Vgw1ugW8gobj4fjJ88kndjvimJKvYHZ6UBntqXLp_roYb_7Tg6YuUXCq08rvTpN4M90klLh0S88uEi6FMCjN_2eovfdMxzsZUz18jYg6FCORavwP4ta4DA50lwY3yiqFXlXy_mezANAcS5qjvbfAE2-rQYXeCGsKJmyDlD4fdmS6TdwoVOwGoGyX1stSAwaZ0zn2iU_vUwB0drjNiGBZk786ZknriufVOWahIR07i1lZOnU3UMhBAF_7POJgHmh4D0bbj2oZZKL2MjK9B8YuNa4MlI-fDstziZ03k2wbVPxApEXsDdqYOmG33R7swHZ7dQhoOPZ0d3q0EMU-iJXlUOACm5Q5gqVxkmzCfRKnWF0KzT7QjUt8lB721S4fhDC-MG0UB1YJpBPZSDb_H5_U1hELCO8ZYrqdK5thRgNH6a0PHoTeEFLZxINPpR4w==",
      "KeyData.PBESalt" : "MMQZXPfdBkeCwtZ2avOGdEs24wmogzHvDTc5TZHOwpY=",
      "KeyData.Iterations" : "10000"
    }
  }
}

 </pre>
 * 
 * 
 * @author Dave
 * @see JSONFormatter
 * 
 */
public class JSONC2Reader {
	
	protected final ObjectMapper mapper;
	protected final Map<String,Object> map;
	protected final Password password; // optional, set if you know the file to be read is in secure mode (-S). 
	
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
			e.printStackTrace();
			throw new RuntimeException(e);
		}
	}

	/**
	 * One use only - password self-destructs after this call
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public Curve25519KeyForPublication parse() {
		// if password not passed in, assume we are in Unsecured form
		if(password == null) {
			C2KeyFormatReader reader = new C2KeyFormatReader(map);
			return reader.read();
		}else{
			// leverage the unlock method in the wrapper class
			// unchecked
			Map<String,Object> keysMap = (Map<String,Object>)map.get("Keys");
			// assume one key present - no checks
			String key = keysMap.keySet().iterator().next();
			final ArmoredPBEResult wrapped = PBEAlg.loadFrom((Map<String,Object>)keysMap.get(key));
			CryptoKeyWrapper wrapper = new CryptoKeyWrapperImpl(wrapped);
			wrapper.unlock(password);
			password.selfDestruct();
			return (Curve25519KeyContents) wrapper.getKeyContents();
		}
	}
}
