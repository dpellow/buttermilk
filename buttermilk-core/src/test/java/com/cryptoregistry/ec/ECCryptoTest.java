/*
 *  This file is part of Buttermilk
 *  Copyright 2011-2014 David R. Smith All Rights Reserved.
 *
 */
package com.cryptoregistry.ec;

import java.io.StringReader;
import java.io.StringWriter;
import java.math.BigInteger;

import org.junit.Assert;
import org.junit.Test;

import com.cryptoregistry.CryptoKeyWrapper;
import com.cryptoregistry.KeyMaterials;
import com.cryptoregistry.formats.JSONFormatter;
import com.cryptoregistry.formats.JSONReader;

import x.org.bouncycastle.math.ec.ECCurve;
import x.org.bouncycastle.math.ec.ECPoint;
import x.org.bouncycastle.util.encoders.Hex;

public class ECCryptoTest {

	/**
	 * Test custom curve construction
	 * 
	 */
	@Test
	public void test0() {

		// use params for P-256, imagine these are a new unnamed curve in the
		// prime field
		// p = 2^224 (2^32 - 1) + 2^192 + 2^96 - 1
		BigInteger p = fromHex("FFFFFFFF00000001000000000000000000000000FFFFFFFFFFFFFFFFFFFFFFFF");
		BigInteger a = fromHex("FFFFFFFF00000001000000000000000000000000FFFFFFFFFFFFFFFFFFFFFFFC");
		BigInteger b = fromHex("5AC635D8AA3A93E7B3EBBD55769886BC651D06B0CC53B0F63BCE3C3E27D2604B");
		byte[] S = Hex.decode("C49D360886E704936A6678E1139D26B7819F7E90");
		BigInteger n = fromHex("FFFFFFFF00000000FFFFFFFFFFFFFFFFBCE6FAADA7179E84F3B9CAC2FC632551");
		BigInteger h = BigInteger.valueOf(1);

		ECCurve curve0 = new ECCurve.Fp(p, a, b);
		ECPoint G0 = curve0
				.decodePoint(Hex
						.decode("04"
								+ "6B17D1F2E12C4247F8BCE6E563A440F277037D812DEB33A0F4A13945D898C296"
								+ "4FE342E2FE1A7F9B8EE7EB4A7C0F9E162BCE33576B315ECECBB6406837BF51F5"));

		// use a custom curve as input - in this case, we just use a known curve
		// definition (P-256)
		ECCustomParameters ucst = new ECFPCustomParameters(p, a, b, S, n, h, G0);
		ECKeyContents contents = CryptoFactory.INSTANCE
				.generateCustomKeys(ucst);

		Assert.assertNotNull(contents);
		JSONFormatter format = new JSONFormatter("Chinese Knees");
		format.add(contents);
		StringWriter writer = new StringWriter();
		format.format(writer);
		String json = writer.toString();
		StringReader reader = new StringReader(writer.toString());
		JSONReader jreader = new JSONReader(reader);
		KeyMaterials km = jreader.parse();
		CryptoKeyWrapper wrapper = km.keys().get(0);
		Assert.assertNotNull(wrapper);
		ECKeyContents contents1 = (ECKeyContents) wrapper.getKeyContents();
		
		Assert.assertNotNull(contents1);
		
		format = new JSONFormatter("Chinese Knees");
		format.add(contents1);
		writer = new StringWriter();
		format.format(writer);
		Assert.assertEquals(json, writer.toString());

	}

	protected static BigInteger fromHex(String hex) {
		return new BigInteger(1, Hex.decode(hex));
	}

}
