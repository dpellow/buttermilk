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

public class Secp160r1 extends ECParametersHolderBase {

	private static final String NAME = "secp160r1";

	private Secp160r1(ECDomainParameters params) {
		super(params);
	}

	static Secp160r1 instance() {
		ECDomainParameters p = init(NAME);
		return new Secp160r1(p);
	}

	private static ECDomainParameters init(String name) {
		// p = 2^160 - 2^31 - 1
		BigInteger p = fromHex("FFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFF7FFFFFFF");
		BigInteger a = fromHex("FFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFF7FFFFFFC");
		BigInteger b = fromHex("1C97BEFC54BD7A8B65ACF89F81D4D4ADC565FA45");
		byte[] S = Hex.decode("1053CDE42C14D696E67687561517533BF3F83345");
		BigInteger n = fromHex("0100000000000000000001F4C8F927AED3CA752257");
		BigInteger h = BigInteger.valueOf(1);

		ECCurve curve = new ECCurve.Fp(p, a, b);

		ECPoint G = curve.decodePoint(Hex.decode("04"
				+ "4A96B5688EF573284664698968C38BB913CBFC82"
				+ "23A628553168947D59DCC912042351377AC5FB32"));

		return new ECDomainParameters(curve, G, n, h, S, name);
	}

	@Override
	public ECDomainParameters createParameters() {
		return cached;
	}

}
