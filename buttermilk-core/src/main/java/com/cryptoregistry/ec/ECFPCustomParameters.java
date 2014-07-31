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
 * Custom parameters for Elliptic curve over the prime field Fp
 * 
 * @author Dave
 *
 */
public class ECFPCustomParameters extends ECCustomParameters {

	private ECFPCustomParameters() {
		super(FIELD.FP, UUID.randomUUID().toString());
		
	}
	
	// public so it can be called from formatting package
	public ECFPCustomParameters(String uuid, LinkedHashMap<String,String> map) {
		super(FIELD.FP, uuid,map);
	}
	
	/**
	 * uuid is user assigned. Encoding cannot be null
	 * 
	 * @param uuid
	 * @param enc
	 * @param p
	 * @param a
	 * @param b
	 * @param seed
	 * @param n
	 * @param h
	 * @param G
	 */
	public ECFPCustomParameters(String uuid, EncodingHint enc, BigInteger p, BigInteger a, BigInteger b, byte [] seed, BigInteger n, BigInteger h, ECPoint G){
		super(FIELD.FP,uuid);
		String _p = FormatUtil.wrap(enc, p);
		String _a = FormatUtil.wrap(enc, a);
		String _b = FormatUtil.wrap(enc, b);
		ArmoredString _seed = new ArmoredString(seed);
		String _n = FormatUtil.wrap(enc, n);
		String _h = FormatUtil.wrap(enc, h);
		String _G = FormatUtil.serializeECPoint(G, enc);
		this.parameters.put("Encoding", enc.toString());
		parameters.put("Field", FIELD.FP.toString());
		this.parameters.put("p", _p);
		this.parameters.put("a", _a);
		this.parameters.put("b", _b);
		this.parameters.put("S", _seed.toString());
		this.parameters.put("n", _n);
		this.parameters.put("h", _h);
		this.parameters.put("G", _G);
	}
	
	/**
	 * Default to Base 16 encoding on the big integers; uuid is randomly constructed
	 * 
	 * @param p
	 * @param a
	 * @param b
	 * @param seed
	 * @param n
	 * @param h
	 * @param G
	 */
	public ECFPCustomParameters(BigInteger p, BigInteger a, BigInteger b, byte [] seed, BigInteger n, BigInteger h, ECPoint G){
		this();
		EncodingHint enc = EncodingHint.Base16;
		String _p = FormatUtil.wrap(enc, p);
		String _a = FormatUtil.wrap(enc, a);
		String _b = FormatUtil.wrap(enc, b);
		ArmoredString _seed = new ArmoredString(seed);
		String _n = FormatUtil.wrap(enc, n);
		String _h = FormatUtil.wrap(enc, h);
		String _G = FormatUtil.serializeECPoint(G, enc);
		this.parameters.put("Encoding", enc.toString());
		parameters.put("Field", FIELD.FP.toString());
		this.parameters.put("p", _p);
		this.parameters.put("a", _a);
		this.parameters.put("b", _b);
		this.parameters.put("S", _seed.toString());
		this.parameters.put("n", _n);
		this.parameters.put("h", _h);
		this.parameters.put("G", _G);
	}
	
	public ECFPCustomParameters clone() {
		LinkedHashMap<String,String> map = new LinkedHashMap<String,String>();
		Iterator<String> iter = parameters.keySet().iterator();
		while(iter.hasNext()){
			String key = iter.next();
			map.put(key, parameters.get(key));
		}
		return new ECFPCustomParameters(uuid,map);
	}

	@Override
	public ECDomainParameters getParameters() {
		
		EncodingHint enc = EncodingHint.valueOf(parameters.get("Encoding"));
		
		 BigInteger a=null,b=null,p=null;
		 if(parameters.containsKey("p")) {
			 p = FormatUtil.unwrap(enc, String.valueOf(parameters.get("p")));
		 }
		 if(parameters.containsKey("a")) {
			 a = FormatUtil.unwrap(enc, String.valueOf(parameters.get("a")));
		 }
		 if(parameters.containsKey("b")) {
			 b = FormatUtil.unwrap(enc, String.valueOf(parameters.get("b")));
		 }
		 
		 ECCurve curve = new ECCurve.Fp(p, a, b);
		 
		 BigInteger n=null,h=null;
		 if(parameters.containsKey("n")) {
			 n = FormatUtil.unwrap(enc, String.valueOf(parameters.get("n")));
		 }
		 if(parameters.containsKey("h")) {
			 h = FormatUtil.unwrap(enc, String.valueOf(parameters.get("h")));
		 }
		 
		 ECPoint G = FormatUtil.parseECPoint(curve, enc, String.valueOf(parameters.get("G")));
		 byte [] S = new ArmoredString(String.valueOf(parameters.get("S"))).decodeToBytes();
			
		 return new ECDomainParameters(curve,G,n,h,S,null);
	}

}
