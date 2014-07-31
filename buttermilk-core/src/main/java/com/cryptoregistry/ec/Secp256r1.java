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

public class Secp256r1 extends ECParametersHolderBase {

	private static final String NAME = "secp256r1";

	private Secp256r1(ECDomainParameters params) {
		super(params);
	}

	static Secp256r1 instance() {
		ECDomainParameters p = init(NAME);
		return new Secp256r1(p);
	}

	// support for alternative - the NIST name
	static Secp256r1 p256() {
		ECDomainParameters p = init("P256");
		return new Secp256r1(p);
	}

	// support for alternative - the NIST name
	static Secp256r1 p256Dash() {
		ECDomainParameters p = init("P-256");
		return new Secp256r1(p);
	}

	private static ECDomainParameters init(String name) {
		// p = 2^224 (2^32 - 1) + 2^192 + 2^96 - 1
		BigInteger p = fromHex("FFFFFFFF00000001000000000000000000000000FFFFFFFFFFFFFFFFFFFFFFFF");
		BigInteger a = fromHex("FFFFFFFF00000001000000000000000000000000FFFFFFFFFFFFFFFFFFFFFFFC");
		BigInteger b = fromHex("5AC635D8AA3A93E7B3EBBD55769886BC651D06B0CC53B0F63BCE3C3E27D2604B");
		byte[] S = Hex.decode("C49D360886E704936A6678E1139D26B7819F7E90");
		BigInteger n = fromHex("FFFFFFFF00000000FFFFFFFFFFFFFFFFBCE6FAADA7179E84F3B9CAC2FC632551");
		BigInteger h = BigInteger.valueOf(1);

		ECCurve curve = new ECCurve.Fp(p, a, b);
		ECPoint G = curve
				.decodePoint(Hex
						.decode("04"
								+ "6B17D1F2E12C4247F8BCE6E563A440F277037D812DEB33A0F4A13945D898C296"
								+ "4FE342E2FE1A7F9B8EE7EB4A7C0F9E162BCE33576B315ECECBB6406837BF51F5"));

		return new ECDomainParameters(curve, G, n, h, S, name);
	}

	@Override
	public ECDomainParameters createParameters() {
		return cached;
	}

}
