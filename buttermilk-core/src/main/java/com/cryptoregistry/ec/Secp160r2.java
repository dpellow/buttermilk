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

public class Secp160r2 extends ECParametersHolderBase {

	private static final String NAME = "secp160r2";

	private Secp160r2(ECDomainParameters params) {
		super(params);
	}

	static Secp160r2 instance() {
		ECDomainParameters p = init(NAME);
		return new Secp160r2(p);
	}

	private static ECDomainParameters init(String name) {
		  // p = 2^160 - 2^32 - 2^14 - 2^12 - 2^9 - 2^8 - 2^7 - 2^3 - 2^2 - 1
        BigInteger p = fromHex("FFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFEFFFFAC73");
        BigInteger a = fromHex("FFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFEFFFFAC70");
        BigInteger b = fromHex("B4E134D3FB59EB8BAB57274904664D5AF50388BA");
        byte[] S = Hex.decode("B99B99B099B323E02709A4D696E6768756151751");
        BigInteger n = fromHex("0100000000000000000000351EE786A818F3A1A16B");
        BigInteger h = BigInteger.valueOf(1);

        ECCurve curve = new ECCurve.Fp(p, a, b);
        //ECPoint G = curve.decodePoint(Hex.decode("02"
        //+ "52DCB034293A117E1F4FF11B30F7199D3144CE6D"));
        ECPoint G = curve.decodePoint(Hex.decode("04"
            + "52DCB034293A117E1F4FF11B30F7199D3144CE6D"
            + "FEAFFEF2E331F296E071FA0DF9982CFEA7D43F2E"));

		return new ECDomainParameters(curve, G, n, h, S, name);
	}

	@Override
	public ECDomainParameters createParameters() {
		return cached;
	}

}
