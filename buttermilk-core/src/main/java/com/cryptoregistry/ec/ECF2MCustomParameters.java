/*
 *  This file is part of Buttermilk
 *  Copyright 2011-2014 David R. Smith All Rights Reserved.
 *
 */
package com.cryptoregistry.ec;

import java.math.BigInteger;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.UUID;

import com.cryptoregistry.formats.EncodingHint;
import com.cryptoregistry.formats.FormatUtil;
import com.cryptoregistry.util.ArmoredString;

import x.org.bouncycastle.crypto.params.ECDomainParameters;
import x.org.bouncycastle.math.ec.ECCurve;
import x.org.bouncycastle.math.ec.ECPoint;

/**
 * Package elliptic curve parameters over the binary field F2m. 
 * 
 * @author Dave
 *
 */
public class ECF2MCustomParameters extends ECCustomParameters {

	private ECF2MCustomParameters() {
		super(FIELD.F2M, UUID.randomUUID().toString());
	}
	
	// public so it can be called from formatting package
	public ECF2MCustomParameters(String uuid, LinkedHashMap<String,String> map) {
		super(FIELD.F2M, uuid,map);
	}
	
	/**
	 * In some scenarios certain parameters will be null. G cannot be null in any case. Uses
	 * Base 16 encoding for BigIntegers, Base 10 in all cases for ints.
	 * 
	 * @param m
	 * @param k1
	 * @param k2
	 * @param k3
	 * @param a
	 * @param b
	 * @param n
	 * @param h
	 * @param G
	 * @param seed
	 */
	public ECF2MCustomParameters( 
			 	int m, 
	            int k1, 
	            int k2, 
	            int k3,
	            BigInteger a, 
	            BigInteger b,
	            BigInteger n,
	            BigInteger h,
	            ECPoint G,
	            byte [] seed) {
		this();
		EncodingHint enc = EncodingHint.Base16;
		String _a = FormatUtil.wrap(enc, a);
		String _b = FormatUtil.wrap(enc, b);
		String _n = FormatUtil.wrap(enc, n);
		String _h = FormatUtil.wrap(enc, h);
		String _G = FormatUtil.serializeECPoint(G, enc);
		ArmoredString _seed = new ArmoredString(seed);
		
		parameters.put("Encoding", enc.toString());
		parameters.put("Field", FIELD.F2M.toString());
		parameters.put("m", String.valueOf(m));
		parameters.put("k1", String.valueOf(k1));
		parameters.put("k2", String.valueOf(k2));
		parameters.put("k3", String.valueOf(k3));
		parameters.put("a", _a);
		parameters.put("b", _b);
		parameters.put("S", _seed.toString());
		parameters.put("n", _n);
		parameters.put("h", _h);
		parameters.put("G", _G);
	
	}
	
	/**
	 * In some scenarios certain parameters will be null. G cannot be null in any case. Uses
	 * Base 16 encoding for BigIntegers, Base 10 in all cases for ints.
	 * 
	 * @param enc
	 * @param uuid
	 * @param m
	 * @param k1
	 * @param k2
	 * @param k3
	 * @param a
	 * @param b
	 * @param n
	 * @param h
	 * @param G
	 * @param seed
	 */
	public ECF2MCustomParameters( 
			    EncodingHint enc,
				String uuid,
			 	int m, 
	            int k1, 
	            int k2, 
	            int k3,
	            BigInteger a, 
	            BigInteger b,
	            BigInteger n,
	            BigInteger h,
	            ECPoint G,
	            byte [] seed) {
		super(FIELD.F2M, uuid);
		
		String _a = FormatUtil.wrap(enc, a);
		String _b = FormatUtil.wrap(enc, b);
		String _n = FormatUtil.wrap(enc, n);
		String _h = FormatUtil.wrap(enc, h);
		String _G = FormatUtil.serializeECPoint(G, enc);
		ArmoredString _seed = new ArmoredString(seed);
		
		parameters.put("Encoding", enc.toString());
		parameters.put("Field", FIELD.F2M.toString());
		parameters.put("m", String.valueOf(m));
		parameters.put("k1", String.valueOf(k1));
		parameters.put("k2", String.valueOf(k2));
		parameters.put("k3", String.valueOf(k3));
		parameters.put("a", _a);
		parameters.put("b", _b);
		parameters.put("S", _seed.toString());
		parameters.put("n", _n);
		parameters.put("h", _h);
		parameters.put("G", _G);
	}
	
	public ECF2MCustomParameters( 
		    EncodingHint enc,
			String uuid,
		 	int m, 
            int k, 
            BigInteger a, 
            BigInteger b,
            BigInteger n,
            BigInteger h,
            ECPoint G,
            byte [] seed) {
	super(FIELD.F2M, uuid);
	
	String _a = FormatUtil.wrap(enc, a);
	String _b = FormatUtil.wrap(enc, b);
	String _n = FormatUtil.wrap(enc, n);
	String _h = FormatUtil.wrap(enc, h);
	String _G = FormatUtil.serializeECPoint(G, enc);
	ArmoredString _seed = new ArmoredString(seed);
	
	parameters.put("Encoding", enc.toString());
	parameters.put("Field", FIELD.F2M.toString());
	parameters.put("m", String.valueOf(m));
	parameters.put("k", String.valueOf(k));
	parameters.put("a", _a);
	parameters.put("b", _b);
	parameters.put("S", _seed.toString());
	parameters.put("n", _n);
	parameters.put("h", _h);
	parameters.put("G", _G);
}
	
	public ECF2MCustomParameters clone() {
		LinkedHashMap<String,String> map = new LinkedHashMap<String,String>();
		Iterator<String> iter = parameters.keySet().iterator();
		while(iter.hasNext()){
			String key = iter.next();
			map.put(key, parameters.get(key));
		}
		return new ECF2MCustomParameters(uuid,map);
	}

	@Override
	public ECDomainParameters getParameters() {
		
		EncodingHint enc = EncodingHint.valueOf(parameters.get("Encoding"));
		
		if(parameters.containsKey("k")) {
			// use the TPB constructor 
			 int m = Integer.parseInt(String.valueOf(parameters.get("m")));
			 int k = Integer.parseInt(String.valueOf(parameters.get("k")));
			 
			 BigInteger a=null,b=null,n=null,h=null;
			 if(parameters.containsKey("a")) {
				 a = FormatUtil.unwrap(enc, String.valueOf(parameters.get("a")));
			 }
			 if(parameters.containsKey("b")) {
				 b = FormatUtil.unwrap(enc, String.valueOf(parameters.get("b")));
			 }
			 if(parameters.containsKey("n")) {
				 n = FormatUtil.unwrap(enc, String.valueOf(parameters.get("n")));
			 }
			 if(parameters.containsKey("h")) {
				 h = FormatUtil.unwrap(enc, String.valueOf(parameters.get("h")));
			 }
			 
			 ECCurve curve = new ECCurve.F2m(m, k, a, b, n, h);
			 
			ECPoint G = FormatUtil.parseECPoint(curve, enc, String.valueOf(parameters.get("G")));
			byte [] S = new ArmoredString(String.valueOf(parameters.get("S"))).decodeToBytes();
			
			return new ECDomainParameters(curve,G,n,h,S,null);
			
		}else if(parameters.containsKey("k1")){
			// use the PPB constructor
			 int m = Integer.parseInt(String.valueOf(parameters.get("m")));
			 int k1 = Integer.parseInt(String.valueOf(parameters.get("k1")));
			 int k2 = Integer.parseInt(String.valueOf(parameters.get("k2")));
			 int k3 = Integer.parseInt(String.valueOf(parameters.get("k3")));
			 
			 BigInteger a=null,b=null,n=null,h=null;
			 if(parameters.containsKey("a")) {
				 a = FormatUtil.unwrap(enc, String.valueOf(parameters.get("a")));
			 }
			 if(parameters.containsKey("b")) {
				 b = FormatUtil.unwrap(enc, String.valueOf(parameters.get("b")));
			 }
			 if(parameters.containsKey("n")) {
				 n = FormatUtil.unwrap(enc, String.valueOf(parameters.get("n")));
			 }
			 if(parameters.containsKey("h")) {
				 h = FormatUtil.unwrap(enc, String.valueOf(parameters.get("h")));
			 }
			 
			 ECCurve curve = new ECCurve.F2m(m, k1, k2, k3, a, b, n, h);
			 
			ECPoint G = FormatUtil.parseECPoint(curve, enc, String.valueOf(parameters.get("G")));
			byte [] S = new ArmoredString(String.valueOf(parameters.get("S"))).decodeToBytes();
			
			return new ECDomainParameters(curve,G,n,h,S,null);
			
		}else{
			throw new RuntimeException("Not sure which ECcurve constructor to use...");
		}
		
	}

}
