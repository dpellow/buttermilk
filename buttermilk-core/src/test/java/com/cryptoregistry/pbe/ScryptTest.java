/*
 *  This file is part of Buttermilk
 *  Copyright 2011-2014 David R. Smith All Rights Reserved.
 *
 */
package com.cryptoregistry.pbe;

import java.io.UnsupportedEncodingException;

import org.junit.Test;

import x.org.bouncycastle.crypto.generators.SCrypt;
import x.org.bouncycastle.util.encoders.Hex;

/**
 * 
 * Test Vectors from
 * 
 * https://tools.ietf.org/html/draft-josefsson-scrypt-kdf-00#section-11
 * 
 * 
 * 
 * @author Dave
 *
 */
public class ScryptTest {

	@Test
	public void test0() throws UnsupportedEncodingException {
		byte[]P= {'p','a','s','s','w','o','r','d'};
		byte[]S= {'N','a','C','l'};
		int N = 8;
		int r = 1024;
		int p = 16;
		int dkLen = 64;
		byte [] key = SCrypt.generate(P, S, N, r, p, dkLen);
		byte [] hex = Hex.encode(key);
		System.err.println(new String(Hex.encode(key)));
	}

}
