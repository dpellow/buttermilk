package com.cryptoregistry.pbe;

import com.cryptoregistry.passwords.Password;
import com.cryptoregistry.passwords.SensitiveBytes;

/**
 * Simple value object with our parameter settings
 * 
 * Scrypt: https://tools.ietf.org/html/draft-josefsson-scrypt-kdf-00
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
	
	// used with Scrypt
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
	
	public PBEParams clone() {
		PBEParams np = new PBEParams(this.getAlg());
		np.setBlockSize_r(this.getBlockSize_r());
		np.setCpuMemoryCost_N(this.getCpuMemoryCost_N());
		np.setDesiredKeyLengthInBytes(this.getDesiredKeyLengthInBytes());
		np.setIterations(this.getIterations());
		np.setIv(this.getIv().clone());
		np.setSalt(this.getSalt().clone());
		np.setParallelization_p(this.getParallelization_p());
		np.setPassword(this.getPassword().clone());
		return np;
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

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((alg == null) ? 0 : alg.hashCode());
		result = prime * result + blockSize_r;
		result = prime * result + cpuMemoryCost_N;
		result = prime * result + desiredKeyLengthInBytes;
		result = prime * result + iterations;
		result = prime * result + ((iv == null) ? 0 : iv.hashCode());
		result = prime * result + parallelization_p;
		result = prime * result
				+ ((password == null) ? 0 : password.hashCode());
		result = prime * result + ((salt == null) ? 0 : salt.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		PBEParams other = (PBEParams) obj;
		if (alg != other.alg)
			return false;
		if (blockSize_r != other.blockSize_r)
			return false;
		if (cpuMemoryCost_N != other.cpuMemoryCost_N)
			return false;
		if (desiredKeyLengthInBytes != other.desiredKeyLengthInBytes)
			return false;
		if (iterations != other.iterations)
			return false;
		if (iv == null) {
			if (other.iv != null)
				return false;
		} else if (!iv.equals(other.iv))
			return false;
		if (parallelization_p != other.parallelization_p)
			return false;
		if (password == null) {
			if (other.password != null)
				return false;
		} else if (!password.equals(other.password))
			return false;
		if (salt == null) {
			if (other.salt != null)
				return false;
		} else if (!salt.equals(other.salt))
			return false;
		return true;
	}

}
