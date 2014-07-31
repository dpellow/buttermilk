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

public class Secp112r2 extends ECParametersHolderBase {

	private static final String NAME = "secp112r2";

	private Secp112r2(ECDomainParameters params) {
		super(params);

	}

	static Secp112r2 instance() {
		ECDomainParameters p = init(NAME);
		return new Secp112r2(p);
	}

	private static ECDomainParameters init(String name) {
		// p = (2^128 - 3) / 76439
		BigInteger p = fromHex("DB7C2ABF62E35E668076BEAD208B");
		BigInteger a = fromHex("6127C24C05F38A0AAAF65C0EF02C");
		BigInteger b = fromHex("51DEF1815DB5ED74FCC34C85D709");
		byte[] S = Hex.decode("002757A1114D696E6768756151755316C05E0BD4");
		BigInteger n = fromHex("36DF0AAFD8B8D7597CA10520D04B");
		BigInteger h = BigInteger.valueOf(4);

		ECCurve curve = new ECCurve.Fp(p, a, b);
		// ECPoint G = curve.decodePoint(Hex.decode("03"
		// + "4BA30AB5E892B4E1649DD0928643"));
		ECPoint G = curve.decodePoint(Hex.decode("04"
				+ "4BA30AB5E892B4E1649DD0928643"
				+ "ADCD46F5882E3747DEF36E956E97"));

		return new ECDomainParameters(curve, G, n, h, S, name);
	}

	@Override
	public ECDomainParameters createParameters() {
		return cached;
	}

}
