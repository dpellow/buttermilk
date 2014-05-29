package com.cryptoregistry.ec;

import java.math.BigInteger;


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
			case "brainpoolP160r1": {
				return CurveFactory.brainpoolP160r1.createParameters();
			}
			
			// TODO add all the rest
			
			default: throw new RuntimeException("unknown curve name: "+name);
		}
	}

	/*
	 * secp112r1
	 */
	private static ECParametersHolder secp112r1 = new ECParametersHolder() {
		
		private ECDomainParameters cached;
		
		public ECDomainParameters createParameters() {
			if(cached != null) return cached;
			// p = (2^128 - 3) / 76439
			BigInteger p = fromHex("DB7C2ABF62E35E668076BEAD208B");
			BigInteger a = fromHex("DB7C2ABF62E35E668076BEAD2088");
			BigInteger b = fromHex("659EF8BA043916EEDE8911702B22");
			byte[] S = Hex.decode("00F50B028E4D696E676875615175290472783FB1");
			BigInteger n = fromHex("DB7C2ABF62E35E7628DFAC6561C5");
			BigInteger h = BigInteger.valueOf(1);

			ECCurve curve = new ECCurve.Fp(p, a, b);
			// ECPoint G = curve.decodePoint(Hex.decode("02"
			// + "09487239995A5EE76B55F9C2F098"));
			ECPoint G = curve.decodePoint(Hex.decode("04"
					+ "09487239995A5EE76B55F9C2F098"
					+ "A89CE5AF8724C0A23E0E0FF77500"));

			cached = new ECDomainParameters(curve, G, n, h, S,"secp112r1");
			return cached;
		}
	};
	
	/*
	 * brainpoolP160r1
	 */
	private static ECParametersHolder brainpoolP160r1 = new ECParametersHolder() {
		
		private ECDomainParameters cached;
		
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
