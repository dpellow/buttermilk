/*
 *  This file is part of Buttermilk
 *  Copyright 2011-2014 David R. Smith All Rights Reserved.
 *
 */package com.cryptoregistry.ec;

import java.math.BigInteger;

import x.org.bouncycastle.crypto.params.ECDomainParameters;
import x.org.bouncycastle.math.ec.ECCurve;
import x.org.bouncycastle.math.ec.ECPoint;
import x.org.bouncycastle.util.encoders.Hex;

public class Secp384r1 extends ECParametersHolderBase {

	private static final String NAME = "secp384r1";

	private Secp384r1(ECDomainParameters params) {
		super(params);
	}

	static Secp384r1 instance() {
		ECDomainParameters p = init(NAME);
		return new Secp384r1(p);
	}
	
	static Secp384r1 p384() {
		ECDomainParameters p = init("P384");
		return new Secp384r1(p);
	}
	
	static Secp384r1 p384Dash() {
		ECDomainParameters p = init("P-384");
		return new Secp384r1(p);
	}

	private static ECDomainParameters init(String name) {
		// p = 2^384 - 2^128 - 2^96 + 2^32 - 1
		BigInteger p = fromHex("FFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFEFFFFFFFF0000000000000000FFFFFFFF");
		BigInteger a = fromHex("FFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFEFFFFFFFF0000000000000000FFFFFFFC");
		BigInteger b = fromHex("B3312FA7E23EE7E4988E056BE3F82D19181D9C6EFE8141120314088F5013875AC656398D8A2ED19D2A85C8EDD3EC2AEF");
		byte[] S = Hex.decode("A335926AA319A27A1D00896A6773A4827ACDAC73");
		BigInteger n = fromHex("FFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFC7634D81F4372DDF581A0DB248B0A77AECEC196ACCC52973");
		BigInteger h = BigInteger.valueOf(1);

		ECCurve curve = new ECCurve.Fp(p, a, b);

		ECPoint G = curve
				.decodePoint(Hex
						.decode("04"
								+ "AA87CA22BE8B05378EB1C71EF320AD746E1D3B628BA79B9859F741E082542A385502F25DBF55296C3A545E3872760AB7"
								+ "3617DE4A96262C6F5D9E98BF9292DC29F8F41DBD289A147CE9DA3113B5F0B8C00A60B1CE1D7E819D7A431D7C90EA0E5F"));

		return new ECDomainParameters(curve, G, n, h, S, name);
	}

	@Override
	public ECDomainParameters createParameters() {
		return cached;
	}

}
