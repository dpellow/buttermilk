package com.cryptoregistry.signature.builder;

import java.util.Iterator;
import java.util.LinkedHashMap;

import com.cryptoregistry.ec.ECKeyForPublication;
import com.cryptoregistry.formats.EncodingHint;
import com.cryptoregistry.formats.FormatUtil;
import com.cryptoregistry.util.MapIterator;
import com.cryptoregistry.util.TimeUtil;

/**
 * Defines the order to sign the contents of an EC-based public key 
 * 
 * @author Dave
 *
 */
public class ECKeyContentsIterator implements MapIterator {

	final ECKeyForPublication pKey;
	final LinkedHashMap<String,String> map;
	Iterator<String> iter;
	
	public ECKeyContentsIterator(ECKeyForPublication pKey) {
		this.pKey = pKey;
		map = new LinkedHashMap<String,String>();
		init(false);
	}
	
	public ECKeyContentsIterator(ECKeyForPublication pKey, boolean verbose) {
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
			map.put(handle+":"+"Q",FormatUtil.serializeECPoint(pKey.Q, EncodingHint.Base64));
			map.put(handle+":"+"CurveName",pKey.curveName);
		}else{
			map.put(handle+":"+"Handle",handle);
			map.put("."+"CreatedOn",TimeUtil.format(pKey.getCreatedOn()));
			map.put("."+"Algorithm",pKey.getKeyAlgorithm().toString());
			map.put("."+"Q",FormatUtil.serializeECPoint(pKey.Q, EncodingHint.Base64));
			map.put("."+"CurveName",pKey.curveName);
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
