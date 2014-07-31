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

public class Secp112r1 extends ECParametersHolderBase {

	private static final String NAME = "secp112r1";

	private Secp112r1(ECDomainParameters params) {
		super(params);
	}

	static Secp112r1 instance() {
		ECDomainParameters p = init(NAME);
		return new Secp112r1(p);
	}

	private static ECDomainParameters init(String name) {
		// p = (2^128 - 3) / 76439
		BigInteger p = fromHex("DB7C2ABF62E35E668076BEAD208B");
		BigInteger a = fromHex("DB7C2ABF62E35E668076BEAD2088");
		BigInteger b = fromHex("659EF8BA043916EEDE8911702B22");
		byte[] S = Hex.decode("00F50B028E4D696E676875615175290472783FB1");
		BigInteger n = fromHex("DB7C2ABF62E35E7628DFAC6561C5");
		BigInteger h = BigInteger.valueOf(1);

		ECCurve curve = new ECCurve.Fp(p, a, b);

		ECPoint G = curve.decodePoint(Hex.decode("04"
				+ "09487239995A5EE76B55F9C2F098"
				+ "A89CE5AF8724C0A23E0E0FF77500"));

		return new ECDomainParameters(curve, G, n, h, S, name);
	}

	@Override
	public ECDomainParameters createParameters() {
		return cached;
	}

}
