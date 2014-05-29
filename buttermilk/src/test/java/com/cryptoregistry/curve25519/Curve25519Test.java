/*
 *  This file is part of Buttermilk
 *  Copyright 2011-2014 David R. Smith All Rights Reserved.
 *
 */

package com.cryptoregistry.curve25519;

import java.io.StringWriter;
import java.nio.file.FileSystem;
import java.nio.file.FileSystems;
import java.nio.file.Path;
import java.util.Iterator;

import org.junit.Test;

import com.cryptoregistry.c2.CryptoFactory;
import com.cryptoregistry.c2.key.Curve25519KeyContents;
import com.cryptoregistry.c2.key.SecretKey;
import com.cryptoregistry.formats.Encoding;
import com.cryptoregistry.formats.Mode;
import com.cryptoregistry.formats.c2.JsonC2KeyFormatter;
import com.cryptoregistry.pbe.PBEParams;
import com.cryptoregistry.pbe.PBEParamsFactory;

import junit.framework.Assert;

public class Curve25519Test {

	@Test
	public void test0() {
		Curve25519KeyContents keys0 = CryptoFactory.INSTANCE.generateKeys();
		Curve25519KeyContents keys1 = CryptoFactory.INSTANCE.generateKeys();
		SecretKey s0 = CryptoFactory.INSTANCE.keyAgreement(keys1.publicKey, keys0.agreementPrivateKey);
		SecretKey s1 = CryptoFactory.INSTANCE.keyAgreement(keys0.publicKey, keys1.agreementPrivateKey);
		Assert.assertTrue(test_equal(s0.getBytes(),s1.getBytes()));
		System.err.println(s0.getBase64Encoding());
		System.err.println(s1.getBase64Encoding());
	}
	
	@Test
	public void test1() {
		Curve25519KeyContents keys = CryptoFactory.INSTANCE.generateKeys();
		JsonC2KeyFormatter f0 = new JsonC2KeyFormatter(keys, null);
		StringWriter w = new StringWriter();
		f0.formatKeys(Mode.OPEN, null, w);
		System.err.println(w.toString());
		
		char[] passwordChars = {'p','a','s','s','w','o','r','d'};
		PBEParams params = PBEParamsFactory.INSTANCE.createPBKDF2Params(passwordChars);
		JsonC2KeyFormatter f1 = new JsonC2KeyFormatter(keys, params);
		w = new StringWriter();
		f1.formatKeys(Mode.SEALED, null, w);
		System.err.println(w.toString());
		
		w = new StringWriter();
		f1.formatKeys(Mode.FOR_PUBLICATION, null, w);
		System.err.println(w.toString());
		
	}
	
	private boolean test_equal(byte[] a, byte[] b) {
		int i;
		for (i = 0; i < 32; i++) {
			if (a[i] != b[i]) {
				return false;
			}
		}
		return true;
	}
	
}
