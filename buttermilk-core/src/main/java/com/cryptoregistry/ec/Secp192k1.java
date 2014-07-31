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

public class Secp192k1 extends ECParametersHolderBase {

	private static final String NAME = "secp192k1";

	private Secp192k1(ECDomainParameters params) {
		super(params);
	}

	static Secp192k1 instance() {
		ECDomainParameters p = init(NAME);
		return new Secp192k1(p);
	}

	private static ECDomainParameters init(String name) {
		 // p = 2^192 - 2^32 - 2^12 - 2^8 - 2^7 - 2^6 - 2^3 - 1
        BigInteger p = fromHex("FFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFEFFFFEE37");
        BigInteger a = ECConstants.ZERO;
        BigInteger b = BigInteger.valueOf(3);
        byte[] S = null;
        BigInteger n = fromHex("FFFFFFFFFFFFFFFFFFFFFFFE26F2FC170F69466A74DEFD8D");
        BigInteger h = BigInteger.valueOf(1);

        ECCurve curve = new ECCurve.Fp(p, a, b);
        //ECPoint G = curve.decodePoint(Hex.decode("03"
        //+ "DB4FF10EC057E9AE26B07D0280B7F4341DA5D1B1EAE06C7D"));
        ECPoint G = curve.decodePoint(Hex.decode("04"
            + "DB4FF10EC057E9AE26B07D0280B7F4341DA5D1B1EAE06C7D"
            + "9B2F2F6D9C5628A7844163D015BE86344082AA88D95E2F9D"));

		return new ECDomainParameters(curve, G, n, h, S, name);
	}

	@Override
	public ECDomainParameters createParameters() {
		return cached;
	}

}
