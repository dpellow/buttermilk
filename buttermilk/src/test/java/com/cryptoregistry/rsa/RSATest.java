package com.cryptoregistry.rsa;

import java.io.StringReader;
import java.io.StringWriter;
import java.io.UnsupportedEncodingException;
import java.security.SecureRandom;

import junit.framework.Assert;

import org.junit.Test;

import com.cryptoregistry.passwords.NewPassword;
import com.cryptoregistry.passwords.Password;
import com.cryptoregistry.passwords.SensitiveBytes;
import com.cryptoregistry.pbe.ArmoredPBEResult;
import com.cryptoregistry.pbe.PBE;
import com.cryptoregistry.pbe.PBEAlg;
import com.cryptoregistry.pbe.PBEParams;
import com.cyptoregistry.formats.Encoding;
import com.cyptoregistry.formats.Mode;
import com.cyptoregistry.formats.rsa.JsonRSAFormatReader;
import com.cyptoregistry.formats.rsa.JsonRSAFormatter;

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
		JsonRSAFormatter formatter = new JsonRSAFormatter(contents,params);
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
		PBEParams paramsV = contents1.generateParams(passV);
		PBE pbe = new PBE(paramsV);
		byte [] plain = pbe.decrypt(contents1.getResultBytes());
		String jsonKey = new String(plain,"UTF-8");
		JsonRSAFormatReader reader2 = new JsonRSAFormatReader(new StringReader(jsonKey));
		RSAKeyContents sealedContents =  reader2.readUnsealedJson();
		
		Assert.assertEquals(contents, sealedContents);
		
		/*
		writer = new StringWriter();
		formatter.formatKeys(Mode.FOR_PUBLICATION, Encoding.Base64url, writer);
		System.err.println(writer.toString());
		*/
	}
	
	@Test
	public void test1() throws UnsupportedEncodingException {
		
		char[]p= {'p','a','s','s','w','o','r','d'};
		Password pass0 = new NewPassword(p);
		byte [] salt0 = {'s', 'a', 'l', 't'};
		SensitiveBytes sb = new SensitiveBytes(salt0);
		
		// needed with script, as the algorithm does not create an IV
		SecureRandom rand = new SecureRandom();
		byte [] iv = new byte[16];
		rand.nextBytes(iv);
		SensitiveBytes ivBytes = new SensitiveBytes(iv);
		
		PBEParams params = new PBEParams(PBEAlg.SCRYPT);
		params.setPassword(pass0);
		params.setSalt(sb);
		params.setIv(ivBytes);
		params.setBlockSize_r(128);
		params.setCpuMemoryCost_N(4);
		params.setDesiredKeyLengthInBytes(32);
		params.setParallelization_p(32);
		
		RSAKeyContents contents = CryptoFactory.INSTANCE.generateKeys();
		JsonRSAFormatter formatter = new JsonRSAFormatter(contents,params);
		
		StringWriter writer = new StringWriter();
		formatter.formatKeys(Mode.SEALED, Encoding.Base64url, writer);
		String sealed = writer.toString();
		System.err.println("sealed: "+sealed);
		
		// now read back in
		JsonRSAFormatReader reader1 = new JsonRSAFormatReader(new StringReader(sealed));
		ArmoredPBEResult contents1 = (ArmoredPBEResult)reader1.read();
		
		char[]pV= {'p','a','s','s','w','o','r','d'};
		Password passV = new NewPassword(pV);
		PBEParams paramsV = contents1.generateParams(passV);
		PBE pbe = new PBE(paramsV);
		byte [] plain = pbe.decrypt(contents1.getResultBytes());
		String jsonKey = new String(plain,"UTF-8");
		System.err.println("Unencrypted: "+jsonKey);
		JsonRSAFormatReader reader2 = new JsonRSAFormatReader(new StringReader(jsonKey));
		RSAKeyContents sealedContents =  reader2.readUnsealedJson();
		
		Assert.assertEquals(contents, sealedContents);
		
	}

}
