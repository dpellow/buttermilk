/*
 *  This file is part of Buttermilk
 *  Copyright 2011-2014 David R. Smith All Rights Reserved.
 *
 */

package com.cryptoregistry.curve25519;

import java.nio.file.FileSystem;
import java.nio.file.FileSystems;
import java.nio.file.Path;
import java.util.Iterator;

import org.junit.Test;

import com.cryptoregistry.curve25519.key.Curve25519KeyContents;
import com.cryptoregistry.curve25519.key.SecretKey;

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
	
	private boolean test_equal(byte[] a, byte[] b) {
		int i;
		for (i = 0; i < 32; i++) {
			if (a[i] != b[i]) {
				return false;
			}
		}
		return true;
	}
	
	@Test
	public void test1() {
		FileSystem fs = FileSystems.getDefault();
		Iterator<Path> iter = fs.getRootDirectories().iterator();
		while(iter.hasNext()){
			System.err.println(iter.next());
		}
	}
}