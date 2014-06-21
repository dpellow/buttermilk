package com.cryptoregistry.signature.builder;

import java.util.Iterator;
import java.util.LinkedHashMap;

import com.cryptoregistry.rsa.RSAKeyForPublication;
import com.cryptoregistry.util.MapIterator;
import com.cryptoregistry.util.TimeUtil;

/**
 * Defines the order to sign the contents of an RSA public key 
 * 
 * @author Dave
 *
 */
public class RSAKeyContentsIterator implements MapIterator {

	final RSAKeyForPublication pKey;
	final LinkedHashMap<String,String> map;
	Iterator<String> iter;
	
	public RSAKeyContentsIterator(RSAKeyForPublication pKey) {
		this.pKey = pKey;
		map = new LinkedHashMap<String,String>();
		init(false);
	}
	
	public RSAKeyContentsIterator(RSAKeyForPublication pKey, boolean verbose) {
		this.pKey = pKey;
		map = new LinkedHashMap<String,String>();
		init(verbose);
	}
	
	private void init(boolean verbose){
		String handle = pKey.getHandle();
		
		if(verbose){
			map.put(handle+":"+"Handle",handle);
			map.put(handle+":"+"CreatedOn",TimeUtil.format(pKey.getCreatedOn()));
			map.put(handle+":"+"Algorithm",pKey.getKeyAlgorithm().toString());
			map.put(handle+":"+"Modulus",pKey.modulus.toString(16));
			map.put(handle+":"+"PublicExponent",pKey.publicExponent.toString(16));
		}else{
			map.put(handle+":"+"Handle",handle);
			map.put("."+"CreatedOn",TimeUtil.format(pKey.getCreatedOn()));
			map.put("."+"Algorithm",pKey.getKeyAlgorithm().toString());
			map.put("."+"Modulus",pKey.modulus.toString(16));
			map.put("."+"PublicExponent",pKey.publicExponent.toString(16));
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
