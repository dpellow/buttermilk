/*
 *  This file is part of Buttermilk
 *  Copyright 2011-2014 David R. Smith All Rights Reserved.
 *
 */
package com.cryptoregistry.ec;

import java.math.BigInteger;

import x.org.bouncycastle.crypto.params.ECDomainParameters;
import x.org.bouncycastle.math.ec.ECConstants;
import x.org.bouncycastle.math.ec.ECCurve;
import x.org.bouncycastle.math.ec.ECPoint;
import x.org.bouncycastle.util.encoders.Hex;

public class Secp224k1 extends ECParametersHolderBase {

	private static final String NAME = "secp224k1";

	private Secp224k1(ECDomainParameters params) {
		super(params);
	}

	static Secp224k1 instance() {
		ECDomainParameters p = init(NAME);
		return new Secp224k1(p);
	}

	private static ECDomainParameters init(String name) {
		// p = 2^224 - 2^32 - 2^12 - 2^11 - 2^9 - 2^7 - 2^4 - 2 - 1
		BigInteger p = fromHex("FFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFEFFFFE56D");
		BigInteger a = ECConstants.ZERO;
		BigInteger b = BigInteger.valueOf(5);
		byte[] S = null;
		BigInteger n = fromHex("010000000000000000000000000001DCE8D2EC6184CAF0A971769FB1F7");
		BigInteger h = BigInteger.valueOf(1);

		ECCurve curve = new ECCurve.Fp(p, a, b);
		// ECPoint G = curve.decodePoint(Hex.decode("03"
		// + "A1455B334DF099DF30FC28A169A467E9E47075A90F7E650EB6B7A45C"));
		ECPoint G = curve.decodePoint(Hex.decode("04"
				+ "A1455B334DF099DF30FC28A169A467E9E47075A90F7E650EB6B7A45C"
				+ "7E089FED7FBA344282CAFBD6F7E319F7C0B0BD59E2CA4BDB556D61A5"));

		return new ECDomainParameters(curve, G, n, h, S, name);
	}

	@Override
	public ECDomainParameters createParameters() {
		return cached;
	}

}
