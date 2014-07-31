/*
 *  This file is part of Buttermilk
 *  Copyright 2011-2014 David R. Smith All Rights Reserved.
 *
 */
package com.cryptoregistry.formats;

import java.io.IOException;

import java.io.StringWriter;
import java.math.BigInteger;
import java.util.concurrent.locks.ReentrantLock;

import x.org.bouncycastle.math.ec.ECCurve;
import x.org.bouncycastle.math.ec.ECPoint;

import com.cryptoregistry.ec.CurveFactory;


import net.iharder.Base64;

public class FormatUtil {
	
	//private static ReentrantLock lock0 = new ReentrantLock();
	private static ReentrantLock lock1 = new ReentrantLock();
	private static ReentrantLock lock2 = new ReentrantLock();
	private static ReentrantLock lock3 = new ReentrantLock();
	private static ReentrantLock lock4 = new ReentrantLock();
	
	public static String wrap(EncodingHint enc, BigInteger bi) {
		
		if(bi == null) return null;
		
		lock1.lock();
		try {
	
			switch (enc) {
			case Base2:
				return bi.toString(2);
			case Base10:
				return bi.toString(10);
			case Base16:
				return bi.toString(16);
			case Base64: {
				return Base64.encodeBytes(bi.toByteArray());
			}
			case Base64url: {
				try {
					return Base64.encodeBytes(bi.toByteArray(), Base64.URL_SAFE);
				} catch (IOException e) {
					throw new RuntimeException(e);
				}
			}
			default:
				throw new RuntimeException("Unknown encoding: " + enc);
			}
			
		}finally{
			lock1.unlock();
		}
	}

	/**
	 * decode a BigInteger. There is some ambiguity in RawBytes, which we will assume is
	 * hex - you should avoid this encoding hint in this scenario
	 * 
	 * @param enc
	 * @param s
	 * @return
	 */
	public static BigInteger unwrap(EncodingHint enc, String s) {
		
		if(s == null) return null;
		
			lock2.lock();
			try {
				
				switch (enc) {
				case Base2:
					return new BigInteger(s,2);
				case Base10:
					return new BigInteger(s,10);
				case Base16:
					return new BigInteger(s,16);
				case RawBytes:
					return new BigInteger(s,16);  //assume RawBytes is hex
				case Base64: {
					try {
						byte [] b = Base64.decode(s);
						return new BigInteger(b);
					} catch (IOException e) {
						throw new RuntimeException(e);
					}
				}
				case Base64url: {
					try {
						byte [] b = Base64.decode(s, Base64.URL_SAFE);
						return new BigInteger(b);
					} catch (IOException e) {
						throw new RuntimeException(e);
					}
				}
				default:
					throw new RuntimeException("Unknown encoding: " + enc);
				}
			
		}finally{
			lock2.unlock();
		}
	}
	
	/**
	 * Convert a point to a string using the Bouncy Castle approach. p cannot be null
	 * 
	 * @param p
	 * @param enc
	 * @return
	 */
	public static String serializeECPoint(ECPoint p, EncodingHint enc){
		lock3.lock();
		try {
			BigInteger biX = new BigInteger(p.getAffineXCoord().toString(),16);
			BigInteger biY  = new BigInteger(p.getAffineYCoord().toString(),16);
			StringWriter buf = new StringWriter();
			buf.append(wrap(enc,biX));
			buf.append(",");
			buf.append(wrap(enc,biY));
			return buf.toString();
		}finally{
			lock3.unlock();
		}
	}
	
	/**
	 * Convert a string encoding back to a point using the Bouncy Castle approach. p cannot be null
	 * 
	 * @param p
	 * @param enc
	 * @return
	 */
	public static ECPoint parseECPoint(String curveName, EncodingHint enc, String in){
		lock4.lock();
		try {
			String [] xy = in.split("\\,");
			BigInteger biX = unwrap(enc,xy[0]);
			BigInteger biY = unwrap(enc,xy[1]);
			ECCurve curve = CurveFactory.getCurveForName(curveName).getCurve();
			return curve.createPoint(biX, biY);
		}finally{
			lock4.unlock();
		}
	}
	
	/**
	 * Used only with custom curves 
	 * 
	 * @param curve
	 * @param enc
	 * @param in
	 * @return
	 */
	public static ECPoint parseECPoint(ECCurve curve, EncodingHint enc, String in){
		lock4.lock();
		try {
			String [] xy = in.split("\\,");
			BigInteger biX = unwrap(enc,xy[0]);
			BigInteger biY = unwrap(enc,xy[1]);
			return curve.createPoint(biX, biY);
		}finally{
			lock4.unlock();
		}
	}

}
