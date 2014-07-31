/*
 *  This file is part of Buttermilk
 *  Copyright 2011-2014 David R. Smith All Rights Reserved.
 *
 */
package com.cryptoregistry.ec;

import java.math.BigInteger;

import x.org.bouncycastle.crypto.params.ECDomainParameters;
import x.org.bouncycastle.math.ec.ECCurve;
import x.org.bouncycastle.util.encoders.Hex;

public class BrainpoolP160r1 extends ECParametersHolderBase {

	private static final String NAME = "brainpoolP160r1";

	private BrainpoolP160r1(ECDomainParameters params) {
		super(params);
	}

	static BrainpoolP160r1 instance() {
		ECDomainParameters p = init(NAME);
		return new BrainpoolP160r1(p);
	}

	private static ECDomainParameters init(String name) {
		
		 ECCurve curve = new ECCurve.Fp(
	                new BigInteger("E95E4A5F737059DC60DFC7AD95B3D8139515620F", 16), // q
	                new BigInteger("340E7BE2A280EB74E2BE61BADA745D97E8F7C300", 16), // a
	                new BigInteger("1E589A8595423412134FAA2DBDEC95C8D8675E58", 16)); // b

	     ECDomainParameters param = new ECDomainParameters(
	                curve,
	                curve.decodePoint(Hex.decode("04BED5AF16EA3F6A4F62938C4631EB5AF7BDBCDBC31667CB477A1A8EC338F94741669C976316DA6321")), // G
	                new BigInteger("E95E4A5F737059DC60DF5991D45029409E60FC09", 16), //n
	                new BigInteger("01", 16), //h
	                null, // seed
	                name); // name
	     
	     return param;
	}

	@Override
	public ECDomainParameters createParameters() {
		return cached;
	}

}
