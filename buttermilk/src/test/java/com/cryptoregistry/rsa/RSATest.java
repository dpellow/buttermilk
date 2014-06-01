package com.cryptoregistry.rsa;

import java.io.StringReader;
import java.io.StringWriter;
import java.io.UnsupportedEncodingException;

import junit.framework.Assert;

import org.junit.Test;

import x.org.bouncycastle.util.Arrays;

import com.cryptoregistry.formats.Encoding;
import com.cryptoregistry.formats.FormatUtil;
import com.cryptoregistry.formats.Mode;
import com.cryptoregistry.passwords.NewPassword;
import com.cryptoregistry.passwords.Password;
import com.cryptoregistry.passwords.SensitiveBytes;
import com.cryptoregistry.pbe.ArmoredPBEResult;
import com.cryptoregistry.pbe.PBEAlg;
import com.cryptoregistry.pbe.PBEParams;
import com.cryptoregistry.pbe.PBEParamsFactory;

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

}
