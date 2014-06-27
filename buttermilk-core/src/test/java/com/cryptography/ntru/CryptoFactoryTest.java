package com.cryptography.ntru;

import java.nio.charset.Charset;

import junit.framework.Assert;

import org.junit.Test;

import x.org.bouncycastle.util.Arrays;

import com.cryptoregistry.ntru.CryptoFactory;
import com.cryptoregistry.ntru.NTRUKeyContents;

public class CryptoFactoryTest {

	@Test
	public void test0() {
		
		byte [] in = "Hello NTRU world".getBytes(Charset.forName("UTF-8"));
		NTRUKeyContents sKey = CryptoFactory.INSTANCE.generateKeys();
		byte [] encrypted = CryptoFactory.INSTANCE.encrypt(sKey, in);
		byte [] out = CryptoFactory.INSTANCE.decrypt(sKey, encrypted);
		
		Assert.assertTrue(Arrays.areEqual(in, out));
	}

}
