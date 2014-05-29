/*
 *  This file is part of Buttermilk
 *  Copyright 2011-2014 David R. Smith All Rights Reserved.
 *
 */
package com.cryptoregistry.formats.rsa;

import java.io.Reader;
import java.math.BigInteger;
import java.util.Date;
import java.util.Iterator;
import java.util.Map;

import com.cryptoregistry.formats.Encoding;
import com.cryptoregistry.formats.FormatUtil;
import com.cryptoregistry.pbe.ArmoredPBKDF2Result;
import com.cryptoregistry.pbe.ArmoredScryptResult;
import com.cryptoregistry.rsa.RSAKeyContents;
import com.cryptoregistry.rsa.RSAKeyForPublication;
import com.cryptoregistry.util.TimeUtil;
import com.fasterxml.jackson.databind.ObjectMapper;

public class JsonRSAFormatReader {

	protected final Map<String,Object> in;
	
	@SuppressWarnings("unchecked")
	public JsonRSAFormatReader(Reader reader) {
		ObjectMapper mapper = new ObjectMapper();
		try {
			in = (Map<String, Object>) mapper.readValue(reader, Map.class);
		} catch (Exception x) {
			throw new RuntimeException(x);
		}
	}
	
	/**
	 * This method parses the JSON and returns different object wrappers based on what was found:
	 * 
	 * 1) for an RSA key formatted with Mode.OPEN, it returns an RSAKeyContents object
	 * 2) for an RSA key formatted with Mode.SEALED, it returns either an instance of ArmoredPBKDF2Result or an ArmoredScryptResult
	 * 3) for an RSA key formatted with Mode.For_PUBLICATION it returns an RSAKeyForPublication object
	 * 
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public Object read() {
		
		String version = (String) in.get("Version");
		Date createdOn = TimeUtil.getISO8601FormatDate((String) in.get("CreatedOn"));
		
		Map<String,Object> keys = (Map<String,Object>) in.get("Keys");
		Iterator<String> iter = keys.keySet().iterator();
		while(iter.hasNext()) {
			String handle = iter.next();
			
			Map<String,Object> entries = (Map<String,Object>) keys.get(handle);
			
			// at this stage we need to determine what mode was used for formatting
			// we can do this by examining the input
			if(entries.containsKey("P")){
				// only present if formatted with OPEN mode
				
				Encoding enc = Encoding.valueOf((String) entries.get("Encoding"));
				
				String val = (String) entries.get("Modulus");
				BigInteger modulus = FormatUtil.unwrap(enc,val);
				val = (String) entries.get("PublicExponent");
				BigInteger publicExponent = FormatUtil.unwrap(enc,val);
				val = (String) entries.get("PrivateExponent");
				BigInteger privateExponent = FormatUtil.unwrap(enc,val);
				val = (String) entries.get("P");
				BigInteger P = FormatUtil.unwrap(enc,val);
				val = (String) entries.get("Q");
				BigInteger Q = FormatUtil.unwrap(enc,val);
				val = (String) entries.get("dP");
				BigInteger dP = FormatUtil.unwrap(enc,val);
				val = (String) entries.get("dQ");
				BigInteger dQ = FormatUtil.unwrap(enc,val);
				val = (String) entries.get("qInv");
				BigInteger qInv = FormatUtil.unwrap(enc,val);
				
				return new RSAKeyContents(version,createdOn,handle, modulus, publicExponent,
						privateExponent, P, Q, dP, dQ, qInv);
				
			}else if(entries.containsKey("KeyData.EncryptedData")){
				// only present if formatted with SEALED mode
				
				// encoding value not used here
				
				String keyData = (String) entries.get("KeyData.EncryptedData");
				String alg = (String) entries.get("KeyData.PBEAlgorithm");
				String salt = (String) entries.get("KeyData.PBESalt");
				
				if(alg.equals("PBKDF2")){
					int iterations = Integer.parseInt((String) entries.get("KeyData.Iterations"));
					return new ArmoredPBKDF2Result(version,createdOn,keyData,salt,iterations);
				}else if(alg.equals("SCRYPT")){
					String keyDataIV = (String) entries.get("KeyData.IV");
					int blockSize = Integer.parseInt((String) entries.get("KeyData.BlockSize"));
					int cpuMemoryCost = Integer.parseInt((String) entries.get("KeyData.CpuMemoryCost"));
					int parallelization = Integer.parseInt((String) entries.get("KeyData.Parallelization"));
					return new ArmoredScryptResult(version,createdOn,keyData,salt,keyDataIV,cpuMemoryCost,blockSize,parallelization);
				}
				
				
			}else{
				
				Encoding enc = Encoding.valueOf((String) entries.get("Encoding"));
				String val = (String) entries.get("Modulus");
				BigInteger modulus = FormatUtil.unwrap(enc,val);
				val = (String) entries.get("PublicExponent");
				BigInteger publicExponent = FormatUtil.unwrap(enc,val);
				
				// we can safely assume the key is for publication
				return new RSAKeyForPublication(version,createdOn,handle,modulus,publicExponent);
			}
		}
		
		return null;
	}
	
	
	
	public RSAKeyContents readUnsealedJson(String version, Date createdOn){
		
		String handle = (String) in.get("Handle");
		Encoding enc = Encoding.valueOf((String) in.get("Encoding"));
		String val = (String) in.get("Modulus");
		BigInteger modulus = FormatUtil.unwrap(enc,val);
		val = (String) in.get("PublicExponent");
		BigInteger publicExponent = FormatUtil.unwrap(enc,val);
		val = (String) in.get("PrivateExponent");
		BigInteger privateExponent = FormatUtil.unwrap(enc,val);
		val = (String) in.get("P");
		BigInteger P = FormatUtil.unwrap(enc,val);
		val = (String) in.get("Q");
		BigInteger Q = FormatUtil.unwrap(enc,val);
		val = (String) in.get("dP");
		BigInteger dP = FormatUtil.unwrap(enc,val);
		val = (String) in.get("dQ");
		BigInteger dQ = FormatUtil.unwrap(enc,val);
		val = (String) in.get("qInv");
		BigInteger qInv = FormatUtil.unwrap(enc,val);
		
		return new RSAKeyContents(version,createdOn,handle, modulus, publicExponent,
				privateExponent, P, Q, dP, dQ, qInv);
	}

}
