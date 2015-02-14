/*
 *  This file is part of Buttermilk(TM) 
 *  Copyright 2013 David R. Smith for cryptoregistry.com
 *
 */
package com.cryptoregistry.client.storage;

import java.util.HashMap;
import java.util.Map;

/**
 * This is for the use-case where we want to find a single item in the key materials such as a key for publication of 
 * type RSA with size 2048 bits, registered to "Bob Smith".
 * 
 * @author Dave
 *
 */
public class SingleResultCriteria implements Criteria {

	public final Map<MetadataTokens, Object> map;
	public SingleResult result;
	
	public SingleResultCriteria() {
		map = new HashMap<MetadataTokens, Object>();
		result = new SingleResult();
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
	public static final SingleResultCriteria c2(String regHandle){
		SingleResultCriteria criteria = new SingleResultCriteria();
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
	public static final SingleResultCriteria c2Hello(String regHandle, String keyHandle){
		SingleResultCriteria criteria = new SingleResultCriteria();
		criteria.put(MetadataTokens.key, true);
		criteria.put(MetadataTokens.forPublication, true);
		criteria.put(MetadataTokens.keyGenerationAlgorithm, "Curve25519");
		criteria.put(MetadataTokens.registrationHandle, regHandle);
		return criteria;
	}
	
	public static final SingleResultCriteria ec(String curveName){
		SingleResultCriteria criteria = new SingleResultCriteria();
		criteria.put(MetadataTokens.key, true);
		criteria.put(MetadataTokens.forPublication, false);
		criteria.put(MetadataTokens.keyGenerationAlgorithm, "EC");
		if(curveName == null) {
			// if null get the first EC key you find
			//criteria.put(MetadataTokens.curveName, curveName);
		}
		return criteria;
	}
	
	public static final SingleResultCriteria rsa(String regHandle){
		SingleResultCriteria criteria = new SingleResultCriteria();
		criteria.put(MetadataTokens.key, true);
		criteria.put(MetadataTokens.forPublication, false);
		criteria.put(MetadataTokens.keyGenerationAlgorithm, "RSA");
		criteria.put(MetadataTokens.registrationHandle, regHandle);
		return criteria;
	}

	@Override
	public String toJSON() {
		// TODO Auto-generated method stub
		return null;
	}
	
	
	
}
