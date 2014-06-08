package com.cryptoregistry.symmetric;

import java.security.SecureRandom;

import junit.framework.Assert;

import org.junit.Test;

public class AESTest {

	@Test
	public void test0() {
		SecureRandom rand = new SecureRandom();
		byte [] key = new byte[32];
		byte [] iv = new byte[16];
		byte [] exampleData = new byte[1024000];
		rand.nextBytes(key);
		rand.nextBytes(iv);
		rand.nextBytes(exampleData);
		
		AESCBCPKCS7 aes = new AESCBCPKCS7(key,iv);
		byte [] encrypted = aes.encrypt(exampleData);
		byte [] unencrypted = aes.decrypt(encrypted);
		
		Assert.assertTrue(test_equal(exampleData,unencrypted));
	}
	
	@Test
	public void test1() {
		SecureRandom rand = new SecureRandom();
		byte [] key = new byte[32];
		byte [] iv = new byte[16];
		byte [] exampleData = new byte[1024000];
		rand.nextBytes(key);
		rand.nextBytes(iv);
		rand.nextBytes(exampleData);
		
		AESGCM aes = new AESGCM(key,iv);
		byte [] encrypted = aes.encrypt(exampleData);
		byte [] unencrypted = aes.decrypt(encrypted);
		
		Assert.assertTrue(test_equal(exampleData,unencrypted));
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
