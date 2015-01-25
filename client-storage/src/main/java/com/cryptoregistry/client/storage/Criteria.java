/*
 *  This file is part of Buttermilk(TM) 
 *  Copyright 2013 David R. Smith for cryptoregistry.com
 *
 */
package com.cryptoregistry.client.storage;

import java.util.HashMap;
import java.util.Map;

public class Criteria {

	public final Map<MetadataTokens, Object> map;
	public Result result;
	
	public Criteria() {
		map = new HashMap<MetadataTokens, Object>();
		result = new Result();
	}

	public Object put(MetadataTokens key, Object value) {
		return map.put(key, value);
	}

	/**
	 * static factory method to find a C2 Key (ours). regHandle can be null, assume local keys are ours
	 * 
	 * @param regHandle
	 * @return
	 */
	public static final Criteria c2(String regHandle){
		Criteria criteria = new Criteria();
		criteria.put(MetadataTokens.key, true);
		criteria.put(MetadataTokens.forPublication, false);
		criteria.put(MetadataTokens.keyGenerationAlgorithm, "Curve25519");
		if(regHandle != null) criteria.put(MetadataTokens.registrationHandle, regHandle);
		return criteria;
	}
	
	/**
	 * static factory method to find a remote key identified in a Hello 
	 * 
	 * @param regHandle
	 * @return
	 */
	public static final Criteria c2Hello(String regHandle, String keyHandle){
		Criteria criteria = new Criteria();
		criteria.put(MetadataTokens.key, true);
		criteria.put(MetadataTokens.forPublication, true);
		criteria.put(MetadataTokens.keyGenerationAlgorithm, "Curve25519");
		criteria.put(MetadataTokens.registrationHandle, regHandle);
		return criteria;
	}
	
	public static final Criteria ec(String curveName){
		Criteria criteria = new Criteria();
		criteria.put(MetadataTokens.key, true);
		criteria.put(MetadataTokens.forPublication, false);
		criteria.put(MetadataTokens.keyGenerationAlgorithm, "EC");
		if(curveName == null) {
			// if null get the first EC key you find
			//criteria.put(MetadataTokens.curveName, curveName);
		}
		return criteria;
	}
	
	public static final Criteria rsa(String regHandle){
		Criteria criteria = new Criteria();
		criteria.put(MetadataTokens.key, true);
		criteria.put(MetadataTokens.forPublication, false);
		criteria.put(MetadataTokens.keyGenerationAlgorithm, "RSA");
		criteria.put(MetadataTokens.registrationHandle, regHandle);
		return criteria;
	}
	
	
}
