package com.cryptoregistry.rsa;

import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;

import junit.framework.Assert;

import org.junit.Test;

import x.org.bouncycastle.crypto.digests.SHA256Digest;
import x.org.bouncycastle.util.Arrays;

import com.cryptoregistry.pbe.PBEParams;
import com.cryptoregistry.pbe.PBEParamsFactory;
import com.cryptoregistry.signature.RSACryptoSignature;

public class RSATest {

	
	
	@Test
	public void test1() throws UnsupportedEncodingException {
		
		char[]passwordChars0= {'p','a','s','s','w','o','r','d'};
		PBEParams params = PBEParamsFactory.INSTANCE.createPBKDF2Params(passwordChars0);
		
		RSAKeyContents contents = CryptoFactory.INSTANCE.generateKeys();
		
		
	}
	
	@Test
	public void test2() throws UnsupportedEncodingException {
		byte [] in = "Test message".getBytes("UTF-8");
		RSAKeyContents contents = CryptoFactory.INSTANCE.generateKeys();
		// test all the different RSA padding available
		for(RSAEngineFactory.Padding pad: RSAEngineFactory.Padding.values()){
			byte [] encrypted = CryptoFactory.INSTANCE.encrypt((RSAKeyForPublication)contents, pad, in);
			byte [] plain = CryptoFactory.INSTANCE.decrypt(contents, pad, encrypted);
			Assert.assertTrue(Arrays.areEqual(in, plain));
		}
	}
	
	@Test
	public void test3() throws UnsupportedEncodingException {
		
		RSAKeyContents contents = CryptoFactory.INSTANCE.generateKeys();
		
		byte [] msgBytes = "this is a test message".getBytes(Charset.forName("UTF-8"));
		SHA256Digest digest = new SHA256Digest();
		digest.update(msgBytes, 0, msgBytes.length);
		byte [] msgHashBytes = new byte[32];
		digest.doFinal(msgHashBytes, 0);
		
		RSACryptoSignature sig = CryptoFactory.INSTANCE.sign("Chinese Eyes", contents, msgHashBytes);
		boolean ok = CryptoFactory.INSTANCE.verify(sig, contents, msgHashBytes);
		Assert.assertTrue(ok);
		
	}

}
