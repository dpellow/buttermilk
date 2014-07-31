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

public class Secp224r1 extends ECParametersHolderBase {

	private static final String NAME = "secp224r1";

	private Secp224r1(ECDomainParameters params) {
		super(params);
	}

	static Secp224r1 instance() {
		ECDomainParameters p = init(NAME);
		return new Secp224r1(p);
	}
	
	static Secp224r1 p224() {
		ECDomainParameters p = init("P224");
		return new Secp224r1(p);
	}
	
	static Secp224r1 p224Dash() {
		ECDomainParameters p = init("P-224");
		return new Secp224r1(p);
	}
	

	private static ECDomainParameters init(String name) {
		// p = 2^224 - 2^96 + 1
		BigInteger p = fromHex("FFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFF000000000000000000000001");
		BigInteger a = fromHex("FFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFEFFFFFFFFFFFFFFFFFFFFFFFE");
		BigInteger b = fromHex("B4050A850C04B3ABF54132565044B0B7D7BFD8BA270B39432355FFB4");
		byte[] S = Hex.decode("BD71344799D5C7FCDC45B59FA3B9AB8F6A948BC5");
		BigInteger n = fromHex("FFFFFFFFFFFFFFFFFFFFFFFFFFFF16A2E0B8F03E13DD29455C5C2A3D");
		BigInteger h = BigInteger.valueOf(1);

		ECCurve curve = new ECCurve.Fp(p, a, b);

		ECPoint G = curve.decodePoint(Hex.decode("04"
				+ "B70E0CBD6BB4BF7F321390B94A03C1D356C21122343280D6115C1D21"
				+ "BD376388B5F723FB4C22DFE6CD4375A05A07476444D5819985007E34"));

		return new ECDomainParameters(curve, G, n, h, S, name);
	}

	@Override
	public ECDomainParameters createParameters() {
		return cached;
	}

}
