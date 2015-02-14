/*
 *  This file is part of Buttermilk(TM) 
 *  Copyright 2013 David R. Smith for cryptoregistry.com
 *
 */
package com.cryptoregistry.client.storage;

import java.io.StringWriter;
import java.util.HashMap;
import java.util.Map;

import com.cryptoregistry.CryptoContact;
import com.cryptoregistry.CryptoKey;
import com.cryptoregistry.ListData;
import com.cryptoregistry.MapData;
import com.cryptoregistry.formats.JSONFormatter;
import com.cryptoregistry.signature.CryptoSignature;

/**
 * This is for the light-weight use-case where we want to find a single item in the key materials such as a key for publication of 
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

	/**
	 * TODO - optimize
	 */
	public String toJSON() {
		String regHandle = (String) map.get(MetadataTokens.registrationHandle);
		if(regHandle == null) {
			throw new RuntimeException("toJSON() requires a Criteria where the MetadataTokens.registrationHandle is set");
		}
		
		JSONFormatter builder = new JSONFormatter(regHandle);
			SingleResult res = this.result;
			if(res.metadata.isIgnore()) {
				throw new RuntimeException("toJSON() cannot JSON format items which are marked 'ignore'");
			}
			if(res.metadata.isKey() && res.metadata.isForPublication()) {
				CryptoKey key = (CryptoKey)res.result;
				builder.add(key);
			}else if(res.metadata.isContact()){
				builder.add((CryptoContact) res.result);
			}else if(res.metadata.isSignature()){
				builder.add((CryptoSignature) res.result);
			}else if(res.metadata.isNamedMap()){
				builder.add((MapData) res.result);
			}else if(res.metadata.isNamedList()){
				builder.add((ListData) res.result);
			}
		
		StringWriter writer = new StringWriter();
		builder.format(writer);
		return writer.toString();
	
	}
	
	
	
}
