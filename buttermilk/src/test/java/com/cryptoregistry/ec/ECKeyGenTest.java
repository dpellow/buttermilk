/*
 *  This file is part of Buttermilk
 *  Copyright 2011-2014 David R. Smith All Rights Reserved.
 *
 */
package com.cryptoregistry.ec;

import java.io.StringWriter;
import java.math.BigInteger;

import junit.framework.Assert;

import org.junit.Test;

import x.org.bouncycastle.math.ec.ECCurve;
import x.org.bouncycastle.math.ec.ECPoint;

import com.cryptoregistry.formats.Encoding;
import com.cryptoregistry.formats.Mode;
import com.cryptoregistry.formats.ec.JsonECKeyFormatter;
import com.cryptoregistry.pbe.PBEParams;
import com.cryptoregistry.pbe.PBEParamsFactory;

public class ECKeyGenTest {

	@Test
	public void test0() {
		String curveName = "secp112r1";
		ECKeyContents ecc = CryptoFactory.INSTANCE.generateKeys(curveName);
		
		String [] xy = ecc.Q.serialize().split("\\,");
		BigInteger biX = new BigInteger(xy[0],16);
		BigInteger biY = new BigInteger(xy[1],16);
		ECCurve curve = CurveFactory.getCurveForName(curveName).getCurve();
		ECPoint recovered = curve.createPoint(biX, biY);
		Assert.assertEquals(ecc.Q.serialize(),recovered.serialize());
	}
	
	@Test
	public void test1() {
		String curveName = "secp112r1";
		ECKeyContents ecc = CryptoFactory.INSTANCE.generateKeys(curveName);
		
		JsonECKeyFormatter f0 = new JsonECKeyFormatter(ecc,null);
		StringWriter w = new StringWriter();
		f0.formatKeys(Mode.OPEN, Encoding.Base16, w);
		System.err.println(w.toString());
		
		char[] passwordChars = {'p','a','s','s','w','o','r','d'};
		
		PBEParams params = PBEParamsFactory.INSTANCE.createPBKDF2Params(passwordChars);
		JsonECKeyFormatter f1 = new JsonECKeyFormatter(ecc, params);
		w = new StringWriter();
		f1.formatKeys(Mode.SEALED, Encoding.Base16, w);
		System.err.println(w.toString());
		
		w = new StringWriter();
		f1.formatKeys(Mode.FOR_PUBLICATION, Encoding.Base16, w);
		System.err.println(w.toString());
	}

}
