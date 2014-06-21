/*
 *  This file is part of Buttermilk
 *  Copyright 2011-2014 David R. Smith All Rights Reserved.
 *
 */
package com.cryptoregistry.ec;

import java.math.BigInteger;

import x.org.bouncycastle.math.ec.ECConstants;

import x.org.bouncycastle.crypto.params.ECDomainParameters;
import x.org.bouncycastle.math.ec.ECCurve;
import x.org.bouncycastle.math.ec.ECPoint;
import x.org.bouncycastle.util.encoders.Hex;

/**
 * Static definitions of the various curves. These are adapted from Bouncy Castle's definitions
 * 
 * @author Dave
 *
 */
public class CurveFactory {
	
	public static boolean curveNameDefined(final String name){
		try {
			return getCurveForName(name) != null;
		}catch(RuntimeException x){
			return false;
		}
	}
	
	public static ECDomainParameters getCurveForName(final String name){
		switch(name){
			case "secp112r1":{
				return CurveFactory.secp112r1.createParameters();
			}
			case "secp112r2":{
				return CurveFactory.secp112r2.createParameters();
			}
			case "secp128r1":{
				return CurveFactory.secp128r1.createParameters();
			}
			case "secp128r2":{
				return CurveFactory.secp128r2.createParameters();
			}
			case "secp160k1":{
				return CurveFactory.secp160k1.createParameters();
			}
			
			//NIST P-256
			
			case "secp256r1":{
				return CurveFactory.secp256r1.createParameters();
			}
			case "P256":{
				return CurveFactory.p256.createParameters();
			}
			case "P-256":{
				return CurveFactory.p256Dash.createParameters();
			}
			
			
			case "brainpoolP160r1": {
				return CurveFactory.brainpoolP160r1.createParameters();
			}
			
			// TODO add all the rest
			
			default: throw new RuntimeException("unknown curve name: "+name);
		}
	}
	
	private static final ECParametersHolder secp112r1 = Secp112r1.instance();
	
	private static final ECParametersHolder secp256r1 = Secp256r1.instance();
	private static final ECParametersHolder p256 = Secp256r1.p256();
	private static final ECParametersHolder p256Dash = Secp256r1.p256Dash();
	
	/*
	 * secp112r2
	 */
	private static ECParametersHolder secp112r2 = new ECParametersHolder() {
		
		private ECDomainParameters cached;
		
		public ECDomainParameters alias(String alias){
			return cached.alias(alias);
		}
		
		public ECDomainParameters createParameters() {
			if(cached != null) return cached;
			 // p = (2^128 - 3) / 76439
            BigInteger p = fromHex("DB7C2ABF62E35E668076BEAD208B");
            BigInteger a = fromHex("6127C24C05F38A0AAAF65C0EF02C");
            BigInteger b = fromHex("51DEF1815DB5ED74FCC34C85D709");
            byte[] S = Hex.decode("002757A1114D696E6768756151755316C05E0BD4");
            BigInteger n = fromHex("36DF0AAFD8B8D7597CA10520D04B");
            BigInteger h = BigInteger.valueOf(4);

            ECCurve curve = new ECCurve.Fp(p, a, b);
            //ECPoint G = curve.decodePoint(Hex.decode("03"
            //+ "4BA30AB5E892B4E1649DD0928643"));
            ECPoint G = curve.decodePoint(Hex.decode("04"
                + "4BA30AB5E892B4E1649DD0928643"
                + "ADCD46F5882E3747DEF36E956E97"));

			cached = new ECDomainParameters(curve, G, n, h, S,"secp112r2");
			return cached;
		}
	};
	
	/*
	 * secp128r1
	 */
	private static ECParametersHolder secp128r1 = new ECParametersHolder() {
		
		private ECDomainParameters cached;
		
		public ECDomainParameters alias(String alias){
			return cached.alias(alias);
		}
		
		public ECDomainParameters createParameters() {
			if(cached != null) return cached;
			 // p = 2^128 - 2^97 - 1
            BigInteger p = fromHex("FFFFFFFDFFFFFFFFFFFFFFFFFFFFFFFF");
            BigInteger a = fromHex("FFFFFFFDFFFFFFFFFFFFFFFFFFFFFFFC");
            BigInteger b = fromHex("E87579C11079F43DD824993C2CEE5ED3");
            byte[] S = Hex.decode("000E0D4D696E6768756151750CC03A4473D03679");
            BigInteger n = fromHex("FFFFFFFE0000000075A30D1B9038A115");
            BigInteger h = BigInteger.valueOf(1);

            ECCurve curve = new ECCurve.Fp(p, a, b);
            //ECPoint G = curve.decodePoint(Hex.decode("03"
            //+ "161FF7528B899B2D0C28607CA52C5B86"));
            ECPoint G = curve.decodePoint(Hex.decode("04"
                + "161FF7528B899B2D0C28607CA52C5B86"
                + "CF5AC8395BAFEB13C02DA292DDED7A83"));

			cached = new ECDomainParameters(curve, G, n, h, S,"secp128r1");
			return cached;
		}
	};
	
	/*
	 * secp128r2
	 */
	private static ECParametersHolder secp128r2 = new ECParametersHolder() {
		
		private ECDomainParameters cached;
		
		public ECDomainParameters alias(String alias){
			return cached.alias(alias);
		}
		
		public ECDomainParameters createParameters() {
			if(cached != null) return cached;
			 // p = 2^128 - 2^97 - 1
            BigInteger p = fromHex("FFFFFFFDFFFFFFFFFFFFFFFFFFFFFFFF");
            BigInteger a = fromHex("D6031998D1B3BBFEBF59CC9BBFF9AEE1");
            BigInteger b = fromHex("5EEEFCA380D02919DC2C6558BB6D8A5D");
            byte[] S = Hex.decode("004D696E67687561517512D8F03431FCE63B88F4");
            BigInteger n = fromHex("3FFFFFFF7FFFFFFFBE0024720613B5A3");
            BigInteger h = BigInteger.valueOf(4);

            ECCurve curve = new ECCurve.Fp(p, a, b);
            //ECPoint G = curve.decodePoint(Hex.decode("02"
            //+ "7B6AA5D85E572983E6FB32A7CDEBC140"));
            ECPoint G = curve.decodePoint(Hex.decode("04"
                + "7B6AA5D85E572983E6FB32A7CDEBC140"
                + "27B6916A894D3AEE7106FE805FC34B44"));

			cached = new ECDomainParameters(curve, G, n, h, S,"secp128r2");
			return cached;
		}
	};
	
	/*
	 * secp160k1
	 */
	private static ECParametersHolder secp160k1 = new ECParametersHolder() {
		
		private ECDomainParameters cached;
		
		public ECDomainParameters alias(String alias){
			return cached.alias(alias);
		}
		
		public ECDomainParameters createParameters() {
			if(cached != null) return cached;
			 // p = 2^160 - 2^32 - 2^14 - 2^12 - 2^9 - 2^8 - 2^7 - 2^3 - 2^2 - 1
            BigInteger p = fromHex("FFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFEFFFFAC73");
            BigInteger a = ECConstants.ZERO;
            BigInteger b = BigInteger.valueOf(7);
            byte[] S = null;
            BigInteger n = fromHex("0100000000000000000001B8FA16DFAB9ACA16B6B3");
            BigInteger h = BigInteger.valueOf(1);

            ECCurve curve = new ECCurve.Fp(p, a, b);
//            ECPoint G = curve.decodePoint(Hex.decode("02"
//                + "3B4C382CE37AA192A4019E763036F4F5DD4D7EBB"));
            ECPoint G = curve.decodePoint(Hex.decode("04"
                + "3B4C382CE37AA192A4019E763036F4F5DD4D7EBB"
                + "938CF935318FDCED6BC28286531733C3F03C4FEE"));

			cached = new ECDomainParameters(curve, G, n, h, S,"secp160k1");
			return cached;
		}
	};
	
	
	
	
	
	/*
	 * brainpoolP160r1
	 */
	private static ECParametersHolder brainpoolP160r1 = new ECParametersHolder() {
		
		private ECDomainParameters cached;
		
		public ECDomainParameters alias(String alias){
			return cached.alias(alias);
		}
		
		public ECDomainParameters createParameters() {
			if(cached != null) return cached;
			
			 ECCurve curve = new ECCurve.Fp(
		                new BigInteger("E95E4A5F737059DC60DFC7AD95B3D8139515620F", 16), // q
		                new BigInteger("340E7BE2A280EB74E2BE61BADA745D97E8F7C300", 16), // a
		                new BigInteger("1E589A8595423412134FAA2DBDEC95C8D8675E58", 16)); // b

		     ECDomainParameters param = new ECDomainParameters(
		                curve,
		                curve.decodePoint(Hex.decode("04BED5AF16EA3F6A4F62938C4631EB5AF7BDBCDBC31667CB477A1A8EC338F94741669C976316DA6321")), // G
		                new BigInteger("E95E4A5F737059DC60DF5991D45029409E60FC09", 16), //n
		                new BigInteger("01", 16), //h
		                null, // seed
		                "brainpoolP160r1"); // name

			cached = param;
			return cached;
		}
	};
	
	

	private static BigInteger fromHex(String hex) {
		return new BigInteger(1, Hex.decode(hex));
	}

}
