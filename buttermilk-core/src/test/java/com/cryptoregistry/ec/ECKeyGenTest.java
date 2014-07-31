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

import com.cryptoregistry.formats.JSONFormatter;

import x.org.bouncycastle.math.ec.ECCurve;
import x.org.bouncycastle.math.ec.ECPoint;


public class ECKeyGenTest {

	@Test
	public void test0() {
		char [] password = {'p','a','s','s','w','w','o','r','d'};
		String curveName = "P-256";
		ECKeyContents ecc = CryptoFactory.INSTANCE.generateKeys(password, curveName);

		final String registrationHandle = "Chinese Eyes";
		JSONFormatter f = new JSONFormatter(registrationHandle);
		f.add(ecc);
		StringWriter writer = new StringWriter();
		f.format(writer);
		System.err.println(writer.toString());
		
	}
	
	@Test
	public void test1() {
		String curveName = "P-256";
		ECKeyContents ecc = CryptoFactory.INSTANCE.generateKeys(curveName);
		
		String [] xy = ecc.Q.serialize().split("\\,");
		BigInteger biX = new BigInteger(xy[0],16);
		BigInteger biY = new BigInteger(xy[1],16);
		ECCurve curve = CurveFactory.getCurveForName(curveName).getCurve();
		ECPoint recovered = curve.createPoint(biX, biY);
		Assert.assertEquals(ecc.Q.serialize(),recovered.serialize());
		

		final String registrationHandle = "Chinese Eyes";
		JSONFormatter f = new JSONFormatter(registrationHandle);
		f.add(ecc);
		StringWriter writer = new StringWriter();
		f.format(writer);
		System.err.println(writer.toString());
		
	}
	

}
