/*
 *  This file is part of Buttermilk
 *  Copyright 2011-2014 David R. Smith All Rights Reserved.
 *
 */
package com.cryptoregistry.pbe;

import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;

import com.cryptoregistry.passwords.NewPassword;
import com.cryptoregistry.passwords.Password;
import com.cryptoregistry.passwords.SensitiveBytes;

public class PBEParamsFactory {

	public final SecureRandom rand;
	public static final int SALT_LENGTH = 32;
	public static final int IV_LENGTH = 16; // 128 bit block size of AES 256
	public static final int PBKDF2_ITERATIONS = 10000;
	
	public static final int CPU_MEMORY_COST = 4;
	public static final int PARALLELIZATION = 32;
	public static final int DESIRED_KEY_LENGTH = 32;
	
	public static final PBEParamsFactory INSTANCE = new PBEParamsFactory();
	
	private PBEParamsFactory() {
		try {
			rand = SecureRandom.getInstance("SHA1PRNG");
		} catch (NoSuchAlgorithmException e) {
			throw new RuntimeException(e);
		}
	}
		
	public PBEParams createPBKDF2Params(char [] passwordChars) {
		Password password = new NewPassword(passwordChars);
		SensitiveBytes salt = new SensitiveBytes(rand,SALT_LENGTH);
		
		PBEParams params = new PBEParams(PBEAlg.PBKDF2);
		params.setSalt(salt);
		params.setIterations(PBKDF2_ITERATIONS);
		params.setPassword(password);
		return params;
	}
	
	/**
	 * Default settings - really you should fine-tune to your setup after this generic call
	 * 
	 * @param passwordChars
	 * @return
	 */
	public PBEParams createScryptParams(char [] passwordChars) {
		
		Password password = new NewPassword(passwordChars);
		SensitiveBytes salt = new SensitiveBytes(rand,SALT_LENGTH);
		SensitiveBytes ivBytes = new SensitiveBytes(rand,IV_LENGTH);
		
		PBEParams params = new PBEParams(PBEAlg.SCRYPT);
		params.setPassword(password);
		params.setSalt(salt);
		params.setIv(ivBytes);
		params.setBlockSize_r(IV_LENGTH*8);
		params.setCpuMemoryCost_N(CPU_MEMORY_COST);
		params.setDesiredKeyLengthInBytes(DESIRED_KEY_LENGTH);
		params.setParallelization_p(PARALLELIZATION);
		return params;
	}
}
