package com.cryptoregistry.formats;

import java.io.IOException;
import java.io.StringReader;
import java.io.StringWriter;
import java.io.UnsupportedEncodingException;
import java.math.BigInteger;
import java.util.concurrent.locks.ReentrantLock;

import x.org.bouncycastle.math.ec.ECCurve;
import x.org.bouncycastle.math.ec.ECPoint;

import com.cryptoregistry.ec.CurveFactory;
import com.cryptoregistry.formats.rsa.JsonRSAFormatReader;
import com.cryptoregistry.passwords.Password;
import com.cryptoregistry.pbe.ArmoredPBEResult;
import com.cryptoregistry.pbe.PBE;
import com.cryptoregistry.pbe.PBEParams;
import com.cryptoregistry.rsa.RSAKeyContents;

import net.iharder.Base64;

public class FormatUtil {
	
	private static ReentrantLock lock0 = new ReentrantLock();
	private static ReentrantLock lock1 = new ReentrantLock();
	private static ReentrantLock lock2 = new ReentrantLock();
	private static ReentrantLock lock3 = new ReentrantLock();
	private static ReentrantLock lock4 = new ReentrantLock();
	
	public static RSAKeyContents extractRSAKeyContents(Password password, ArmoredPBEResult wrapper){
		lock0.lock();
		try {
			PBEParams params = wrapper.generateParams(password);
			PBE pbe = new PBE(params);
			byte [] plain = pbe.decrypt(wrapper.getResultBytes());
			String jsonKey;
			try {
				jsonKey = new String(plain,"UTF-8");
				JsonRSAFormatReader reader = new JsonRSAFormatReader(new StringReader(jsonKey));
				return reader.readUnsealedJson(wrapper.version,wrapper.createdOn);
			} catch (UnsupportedEncodingException e) {
				throw new RuntimeException(e);
			}
		}finally{
			lock0.unlock();
		}
	
	}
	
	public static String wrap(Encoding enc, BigInteger bi) {
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

	public static BigInteger unwrap(Encoding enc, String s) {
			lock2.lock();
			try {
				
				switch (enc) {
				case Base2:
					return new BigInteger(s,2);
				case Base10:
					return new BigInteger(s,10);
				case Base16:
					return new BigInteger(s,16);
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
	
	public static String serializeECPoint(ECPoint p, Encoding enc){
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
	
	public static ECPoint parseECPoint(String curveName, Encoding enc, String in){
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

}
