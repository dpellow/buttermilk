package com.cryptoregistry.pbe;

import java.util.Map;

/**
 * Algorithms we currently support for key derivation when encrypting key materials for Secure mode
 * 
 * @author Dave
 *
 */
public enum PBEAlg {
	PBKDF2,SCRYPT;
	
	public static ArmoredPBEResult loadFrom(Map<String,Object> keyData){
		
		String pbeAlg = (String) keyData.get("KeyData.PBEAlgorithm");
		PBEAlg algEnum = null;
		
		try {
			algEnum = PBEAlg.valueOf(pbeAlg);
		}catch(IllegalArgumentException x){
			throw new RuntimeException("Unknown Alg: "+pbeAlg);
		}
		
		switch(algEnum){
			case SCRYPT:{
				String encryptedData = (String) keyData.get("KeyData.EncryptedData");
				String salt = (String) keyData.get("KeyData.PBESalt");
				String iv = (String) keyData.get("KeyData.IV");
				int blockSize = Integer.parseInt((String) keyData.get("KeyData.BlockSize"));
				int cpuCost = Integer.parseInt((String) keyData.get("KeyData.CpuMemoryCost"));
				int para = Integer.parseInt((String) keyData.get("KeyData.Parallelization"));
				return new ArmoredScryptResult(encryptedData,salt,iv,blockSize,cpuCost,para);
			}
			case PBKDF2: {
				String encryptedData = (String) keyData.get("KeyData.EncryptedData");
				String salt = (String) keyData.get("KeyData.PBESalt");
				int iterations = Integer.parseInt((String) keyData.get("KeyData.Iterations"));
				return new ArmoredPBKDF2Result(encryptedData,salt,iterations);
			}
			default: {
				// should never get here, but satisfy compiler
				throw new RuntimeException("Sorry, don't recognize "+pbeAlg);
			}
		}
	}
}
