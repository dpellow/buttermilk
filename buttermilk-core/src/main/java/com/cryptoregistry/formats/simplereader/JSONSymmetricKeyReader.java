/*
 *  This file is part of Buttermilk
 *  Copyright 2011-2014 David R. Smith All Rights Reserved.
 *
 */
package com.cryptoregistry.formats.simplereader;

import java.io.File;
import java.io.Reader;
import java.util.Map;

import com.cryptoregistry.CryptoKeyWrapper;
import com.cryptoregistry.CryptoKeyWrapperImpl;
import com.cryptoregistry.formats.SymmetricKeyFormatReader;
import com.cryptoregistry.passwords.Password;
import com.cryptoregistry.pbe.ArmoredPBEResult;
import com.cryptoregistry.pbe.PBEAlg;
import com.cryptoregistry.symmetric.SymmetricKeyContents;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * 
 * <p>This class assumes you are reading a very simple file essentially like one of those below. It can read
 * unsecured and secured modes. To read a secured mode you must have provided the password.
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
      "KeyAlgorithm" : "Symmetric",
      "CreatedOn" : "2014-07-31T09:59:27+0000",
      "Encoding" : "Base64url",
      "s" : "qi3cgft8KPxH1znyHFkadYnRzV71FO9LFO7Zk4WsPQI="
    }
  }
}

Secured Mode:

{
  "Version" : "Buttermilk Key Materials 1.0",
  "RegHandle" : "Chinese Knees",
  "Keys" : {
    "c7a12163-e184-4ea2-bdc1-9a3a7c0e1f7f-S" : {
      "KeyData.Type" : "Symmetric",
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
public class JSONSymmetricKeyReader {
	
	protected final ObjectMapper mapper;
	protected final Map<String,Object> map;
	protected final Password password; // optional, set if you know the file to be read is in secure mode (-S). 
	
	/**
	 * Fails immediately if anything goes wrong reading the file or parsing
	 * 
	 * @param path
	 */
	@SuppressWarnings("unchecked")
	public JSONSymmetricKeyReader(File path) {
		
		password = null;
		mapper = new ObjectMapper();
		try {
			map = mapper.readValue(path, Map.class);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
	
	@SuppressWarnings("unchecked")
	public JSONSymmetricKeyReader(Reader in) {
		
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
	public JSONSymmetricKeyReader(File path, Password password){

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
	public JSONSymmetricKeyReader(Reader in, Password password) {
		
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
	 * One use only - password object self-destructs after this call
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public SymmetricKeyContents parse() {
		// if password not passed in, assume we are in Unsecured form
		if(password == null) {
			SymmetricKeyFormatReader reader = new SymmetricKeyFormatReader(map);
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
			return (SymmetricKeyContents) wrapper.getKeyContents();
		}
	}
}
