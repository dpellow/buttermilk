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

public class Secp192r1 extends ECParametersHolderBase {

	private static final String NAME = "secp192r1";

	private Secp192r1(ECDomainParameters params) {
		super(params);
	}

	static Secp192r1 instance() {
		ECDomainParameters p = init(NAME);
		return new Secp192r1(p);
	}

	private static ECDomainParameters init(String name) {
		 // p = 2^192 - 2^64 - 1
        BigInteger p = fromHex("FFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFEFFFFFFFFFFFFFFFF");
        BigInteger a = fromHex("FFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFEFFFFFFFFFFFFFFFC");
        BigInteger b = fromHex("64210519E59C80E70FA7E9AB72243049FEB8DEECC146B9B1");
        byte[] S = Hex.decode("3045AE6FC8422F64ED579528D38120EAE12196D5");
        BigInteger n = fromHex("FFFFFFFFFFFFFFFFFFFFFFFF99DEF836146BC9B1B4D22831");
        BigInteger h = BigInteger.valueOf(1);

        ECCurve curve = new ECCurve.Fp(p, a, b);
     
        ECPoint G = curve.decodePoint(Hex.decode("04"
            + "188DA80EB03090F67CBF20EB43A18800F4FF0AFD82FF1012"
            + "07192B95FFC8DA78631011ED6B24CDD573F977A11E794811"));

		return new ECDomainParameters(curve, G, n, h, S, name);
	}

	@Override
	public ECDomainParameters createParameters() {
		return cached;
	}

}
