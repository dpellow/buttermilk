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
import com.cryptoregistry.formats.rsa.JsonRSAFormatReader;
import com.cryptoregistry.formats.rsa.JsonRSAKeyFormatter;
import com.cryptoregistry.passwords.NewPassword;
import com.cryptoregistry.passwords.Password;
import com.cryptoregistry.passwords.SensitiveBytes;
import com.cryptoregistry.pbe.ArmoredPBEResult;
import com.cryptoregistry.pbe.PBEAlg;
import com.cryptoregistry.pbe.PBEParams;
import com.cryptoregistry.pbe.PBEParamsFactory;

public class RSATest {

	@Test
	public void test0() throws UnsupportedEncodingException {
		
		char[]p= {'p','a','s','s','w','o','r','d'};
		Password pass0 = new NewPassword(p);
		byte [] salt0 = {'s', 'a', 'l', 't'};
		SensitiveBytes sb = new SensitiveBytes(salt0);
		int iterations = 10000;
		
		PBEParams params = new PBEParams(PBEAlg.PBKDF2);
		params.setSalt(sb);
		params.setIterations(iterations);
		params.setPassword(pass0);
		
		RSAKeyContents contents = CryptoFactory.INSTANCE.generateKeys();
		JsonRSAKeyFormatter formatter = new JsonRSAKeyFormatter(contents,params);
		StringWriter writer = new StringWriter();
		formatter.formatKeys(Mode.OPEN, Encoding.Base64url, writer);
		String open = writer.toString();
		System.err.println(open);
		JsonRSAFormatReader reader0 = new JsonRSAFormatReader(new StringReader(open));
		RSAKeyContents contents0 = (RSAKeyContents) reader0.read();
		
		Assert.assertEquals(contents, contents0);
		
		writer = new StringWriter();
		formatter.formatKeys(Mode.SEALED, Encoding.Base64url, writer);
		String sealed = writer.toString();
		JsonRSAFormatReader reader1 = new JsonRSAFormatReader(new StringReader(sealed));
		ArmoredPBEResult contents1 = (ArmoredPBEResult)reader1.read();
		
		char[]pV= {'p','a','s','s','w','o','r','d'};
		Password passV = new NewPassword(pV);
		//PBEParams paramsV = contents1.generateParams(passV);
		//PBE pbe = new PBE(paramsV);
		//byte [] plain = pbe.decrypt(contents1.getResultBytes());
		//String jsonKey = new String(plain,"UTF-8");
		//JsonRSAFormatReader reader2 = new JsonRSAFormatReader(new StringReader(jsonKey));
		//RSAKeyContents sealedContents =  reader2.readUnsealedJson(contents1.version,contents1.createdOn);
		
		RSAKeyContents sealedContents = FormatUtil.extractRSAKeyContents(passV, contents1);
		
		Assert.assertEquals(contents, sealedContents);
		
		/*
		writer = new StringWriter();
		formatter.formatKeys(Mode.FOR_PUBLICATION, Encoding.Base64url, writer);
		System.err.println(writer.toString());
		*/
	}
	
	@Test
	public void test1() throws UnsupportedEncodingException {
		
		char[]passwordChars0= {'p','a','s','s','w','o','r','d'};
		PBEParams params = PBEParamsFactory.INSTANCE.createPBKDF2Params(passwordChars0);
		
		RSAKeyContents contents = CryptoFactory.INSTANCE.generateKeys();
		JsonRSAKeyFormatter formatter = new JsonRSAKeyFormatter(contents,params);
		
		StringWriter writer = new StringWriter();
		formatter.formatKeys(Mode.SEALED, Encoding.Base64url, writer);
		String sealed = writer.toString();
		System.err.println("sealed: "+sealed);
		
		// now read back in
		JsonRSAFormatReader reader1 = new JsonRSAFormatReader(new StringReader(sealed));
		ArmoredPBEResult sealedResult = (ArmoredPBEResult)reader1.read();
		
		char[]passwordChars1= {'p','a','s','s','w','o','r','d'};
		NewPassword password1 = new NewPassword(passwordChars1);
		RSAKeyContents sealedContents = FormatUtil.extractRSAKeyContents(password1, sealedResult);
		
		Assert.assertEquals(contents, sealedContents);
		
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
