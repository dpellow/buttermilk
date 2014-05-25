package com.cryptoregistry.pbe;


import java.security.SecureRandom;

import junit.framework.Assert;

import org.junit.Test;

import x.org.bouncycastle.crypto.generators.SCrypt;
import x.org.bouncycastle.util.Arrays;

import com.cryptoregistry.passwords.NewPassword;
import com.cryptoregistry.passwords.Password;
import com.cryptoregistry.passwords.SensitiveBytes;

public class PBETest {

	@Test
	public void test0() throws Exception {
		
		byte [] input = "input test is a lot of tiny bytes".getBytes("UTF-8");
		
		char[]p= {'p','a','s','s','w','o','r','d'};
		Password pass0 = new NewPassword(p);
		byte [] salt0 = {'s', 'a', 'l', 't'};
		SensitiveBytes sb = new SensitiveBytes(salt0);
		int iterations = 100;
		
		PBEParams params = new PBEParams(PBEAlg.PBKDF2);
		params.setSalt(sb);
		params.setIterations(iterations);
		params.setPassword(pass0);
		
		PBE pbe0 = new PBE(params);
		ArmoredPBKDF2Result result = (ArmoredPBKDF2Result) pbe0.encrypt(input);
		
		// now decrypt
		
		char[]q= {'p','a','s','s','w','o','r','d'};
		Password pass1 = new NewPassword(q);
		params = new PBEParams(PBEAlg.PBKDF2);
		SensitiveBytes sb1 = new SensitiveBytes(result.getSaltBytes());
		params.setSalt(sb1);
		params.setIterations(result.iterations);
		params.setPassword(pass1);
		
		PBE pbe1 = new PBE(params);
		byte [] output = pbe1.decrypt(result.getResultBytes());
		
		Assert.assertTrue(Arrays.areEqual(input, output));
		
	}
	
	@Test
	public void test1() throws Exception {
		
		byte [] input = "input test is a lot of tiny bytes".getBytes("UTF-8");
		
		SecureRandom rand = new SecureRandom();
		byte [] iv = new byte[16];
		rand.nextBytes(iv);
		
		char[]p= {'p','a','s','s','w','o','r','d'};
		Password pass0 = new NewPassword(p);
		byte [] salt0 = {'s', 'a', 'l', 't'};
		SensitiveBytes sb = new SensitiveBytes(salt0);
		
		PBEParams params = new PBEParams(PBEAlg.SCRYPT);
		params.setPassword(pass0);
		params.setSalt(sb);
		params.setIv(new SensitiveBytes(iv));
		params.setBlockSize_r(128);
		params.setCpuMemoryCost_N(4);
		params.setDesiredKeyLengthInBytes(32);
		params.setParallelization_p(32);
		
		PBE pbe0 = new PBE(params);
		ArmoredScryptResult result = (ArmoredScryptResult) pbe0.encrypt(input);
		
		// now decrypt
		
		char[]q= {'p','a','s','s','w','o','r','d'};
		Password pass1 = new NewPassword(q);
		params = new PBEParams(PBEAlg.SCRYPT);
		SensitiveBytes sb1 = new SensitiveBytes(result.getSaltBytes());
		params.setPassword(pass1);
		params.setSalt(sb1);
		params.setIv(new SensitiveBytes(result.getIVBytes()));
		
		PBE pbe1 = new PBE(params);
		byte [] output = pbe1.decrypt(result.getResultBytes());
		
		Assert.assertTrue(Arrays.areEqual(input, output));
	}
	
	/**
	 * @param r the block size parameter.
	 * @param N the CPU/Memory cost parameter.
	 * @param p the parallelization parameter.
	 * @param dkLen the desired length for derived keys, in bytes.
	 * */
	
	@Test
	public void test2() {
		byte[]P= {'p','a','s','s','w','o','r','d'};
		byte[]S= {'s','a','l','t'};
		int N = 4;
		int r = 128;
		int p = 32;
		int dkLen = 32;
		byte [] key = SCrypt.generate(P, S, N, r, p, dkLen);
		System.err.println(java.util.Arrays.toString(key));
	}

}
