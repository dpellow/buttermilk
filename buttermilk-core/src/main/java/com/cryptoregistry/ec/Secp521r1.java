/*
 *  This file is part of Buttermilk
 *  Copyright 2011-2014 David R. Smith All Rights Reserved.
 *
 */
package com.cryptoregistry.ec;

import java.math.BigInteger;

import x.org.bouncycastle.crypto.params.ECDomainParameters;
import x.org.bouncycastle.math.ec.ECCurve;
import x.org.bouncycastle.math.ec.ECPoint;
import x.org.bouncycastle.util.encoders.Hex;

public class Secp521r1 extends ECParametersHolderBase {

	private static final String NAME = "secp521r1";

	private Secp521r1(ECDomainParameters params) {
		super(params);
	}

	static Secp521r1 instance() {
		ECDomainParameters p = init(NAME);
		return new Secp521r1(p);
	}

	// support for alternative - the NIST name
	static Secp521r1 p521() {
		ECDomainParameters p = init("P521");
		return new Secp521r1(p);
	}
	
	// support for alternative - the NIST name
	static Secp521r1 p521Dash() {
		ECDomainParameters p = init("P-521");
		return new Secp521r1(p);
	}

	private static ECDomainParameters init(String name) {
		// p = 2^521 - 1
		BigInteger p = fromHex("01FFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFF");
		BigInteger a = fromHex("01FFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFC");
		BigInteger b = fromHex("0051953EB9618E1C9A1F929A21A0B68540EEA2DA725B99B315F3B8B489918EF109E156193951EC7E937B1652C0BD3BB1BF073573DF883D2C34F1EF451FD46B503F00");
		byte[] S = Hex.decode("D09E8800291CB85396CC6717393284AAA0DA64BA");
		BigInteger n = fromHex("01FFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFA51868783BF2F966B7FCC0148F709A5D03BB5C9B8899C47AEBB6FB71E91386409");
		BigInteger h = BigInteger.valueOf(1);

		ECCurve curve = new ECCurve.Fp(p, a, b);

		ECPoint G = curve
				.decodePoint(Hex
						.decode("04"
								+ "00C6858E06B70404E9CD9E3ECB662395B4429C648139053FB521F828AF606B4D3DBAA14B5E77EFE75928FE1DC127A2FFA8DE3348B3C1856A429BF97E7E31C2E5BD66"
								+ "011839296A789A3BC0045C8A5FB42C7D1BD998F54449579B446817AFBD17273E662C97EE72995EF42640C550B9013FAD0761353C7086A272C24088BE94769FD16650"));

		return new ECDomainParameters(curve, G, n, h, S, name);
	}

	@Override
	public ECDomainParameters createParameters() {
		return cached;
	}

}
