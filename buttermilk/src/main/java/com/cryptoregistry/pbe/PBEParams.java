package com.cryptoregistry.pbe;

import com.cryptoregistry.passwords.Password;
import com.cryptoregistry.passwords.SensitiveBytes;

/**
 * Simple value object with our parameter settings
 * 
 * @author Dave
 *
 */
public class PBEParams {

	// used by both routines
	private PBEAlg alg;
	private Password password;
	private SensitiveBytes salt;
	
	private int iterations; // set this if using PBKDF2
	
	// used with SCrypt
	private SensitiveBytes iv;
	
	// set these if using SCrypt, or else rely on the defaults
	private int cpuMemoryCost_N = 4;
	private int blockSize_r = 128;
	private int parallelization_p = 32;
	
	// for scrypt, needs to be 32 to work with our code (used as 256 bit AES key)
	private int desiredKeyLengthInBytes = 32;
	
	public PBEParams(PBEAlg alg) {
		this.alg = alg;
	}

	public PBEAlg getAlg() {
		return alg;
	}

	public void setAlg(PBEAlg alg) {
		this.alg = alg;
	}

	public Password getPassword() {
		return password;
	}

	public void setPassword(Password password) {
		this.password = password;
	}

	public SensitiveBytes getSalt() {
		return salt;
	}

	public void setSalt(SensitiveBytes salt) {
		this.salt = salt;
	}

	public int getIterations() {
		return iterations;
	}

	public void setIterations(int iterations) {
		this.iterations = iterations;
	}

	public int getCpuMemoryCost_N() {
		return cpuMemoryCost_N;
	}

	public void setCpuMemoryCost_N(int cpuMemoryCost_N) {
		this.cpuMemoryCost_N = cpuMemoryCost_N;
	}

	public int getBlockSize_r() {
		return blockSize_r;
	}

	public void setBlockSize_r(int blockSize_r) {
		this.blockSize_r = blockSize_r;
	}

	public int getParallelization_p() {
		return parallelization_p;
	}

	public void setParallelization_p(int parallelization_p) {
		this.parallelization_p = parallelization_p;
	}

	public int getDesiredKeyLengthInBytes() {
		return desiredKeyLengthInBytes;
	}

	public void setDesiredKeyLengthInBytes(int desiredKeyLengthInBytes) {
		this.desiredKeyLengthInBytes = desiredKeyLengthInBytes;
	}

	public SensitiveBytes getIv() {
		return iv;
	}

	public void setIv(SensitiveBytes iv) {
		this.iv = iv;
	}

}
