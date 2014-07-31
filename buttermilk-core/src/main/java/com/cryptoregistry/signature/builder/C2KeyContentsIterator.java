/*
 *  This file is part of Buttermilk
 *  Copyright 2011-2014 David R. Smith All Rights Reserved.
 *
 */
package com.cryptoregistry.signature.builder;

import java.util.Iterator;
import java.util.LinkedHashMap;

import com.cryptoregistry.c2.key.Curve25519KeyForPublication;
import com.cryptoregistry.util.MapIterator;
import com.cryptoregistry.util.TimeUtil;

/**
 * Defines the order to sign the contents of an EC-based public key 
 * 
 * @author Dave
 *
 */
public class C2KeyContentsIterator implements MapIterator {

	final Curve25519KeyForPublication pKey;
	final LinkedHashMap<String,String> map;
	Iterator<String> iter;
	
	public C2KeyContentsIterator(Curve25519KeyForPublication pKey) {
		this.pKey = pKey;
		map = new LinkedHashMap<String,String>();
		init(false);
	}
	
	public C2KeyContentsIterator(Curve25519KeyForPublication pKey, boolean verbose) {
		this.pKey = pKey;
		map = new LinkedHashMap<String,String>();
		init(verbose);
	}
	
	/**
	 * We always encode the public point Q in Encoding Base64
	 * 
	 * @param verbose
	 */
	private void init(boolean verbose){
		String handle = pKey.getHandle();
		
		if(verbose){
			map.put(handle+":"+"Handle",handle);
			map.put(handle+":"+"CreatedOn",TimeUtil.format(pKey.getCreatedOn()));
			map.put(handle+":"+"Algorithm",pKey.getKeyAlgorithm().toString());
			map.put(handle+":"+"P",pKey.publicKey.getBase64Encoding());
		}else{
			map.put(handle+":"+"Handle",handle);
			map.put("."+"CreatedOn",TimeUtil.format(pKey.getCreatedOn()));
			map.put("."+"Algorithm",pKey.getKeyAlgorithm().toString());
			map.put("."+"P",pKey.publicKey.getBase64Encoding());
		}
	}

	@Override
	public boolean hasNext() {
		if(iter == null) iter = map.keySet().iterator();
		return iter.hasNext();
	}

	@Override
	public String next() {
		return iter.next();
	}
	
	@Override
	public String get(String key){
		return map.get(key);
	}
	
	public String getHandle() {
		return pKey.getHandle();
	}

	@Override
	public void remove() {
		iter.remove();
	}
	
	public void reset(){
		if(!iter.hasNext()) iter = map.keySet().iterator();
	}

}
