package com.cryptoregistry.signature.builder;

import java.util.Iterator;
import java.util.LinkedHashMap;

import com.cryptoregistry.rsa.RSAKeyForPublication;
import com.cryptoregistry.util.TimeUtil;

/**
 * Defines the order to sign the contents of an RSA public key
 * 
 * @author Dave
 *
 */
public class RSAKeyContentsIterator implements Iterator<String> {

	final RSAKeyForPublication pKey;
	final LinkedHashMap<String,String> map;
	Iterator<String> iter;
	
	public RSAKeyContentsIterator(RSAKeyForPublication pKey) {
		this.pKey = pKey;
		map = new LinkedHashMap<String,String>();
		init();
	}
	
	private void init(){
		String handle = pKey.getHandle();
		map.put(handle+":"+"Handle",handle);
		map.put(handle+":"+"CreatedOn",TimeUtil.format(pKey.getCreatedOn()));
		map.put(handle+":"+"Algorithm",pKey.getKeyAlgorithm().toString());
		map.put(handle+":"+"Modulus",pKey.modulus.toString(16));
		map.put(handle+":"+"PublicExponent",pKey.publicExponent.toString(16));
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
	
	public String get(String key){
		return map.get(key);
	}

	@Override
	public void remove() {
		iter.remove();
	}
	
	public void reset(){
		if(!iter.hasNext()) iter = map.keySet().iterator();
	}

}
