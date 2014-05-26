package com.cryptoregistry.pbe;

import java.io.IOException;
import java.util.Date;

import com.cryptoregistry.passwords.Password;
import com.cryptoregistry.passwords.SensitiveBytes;

import net.iharder.Base64;

public class ArmoredScryptResult extends ArmoredPBEResult {

	public final int cpuMemoryCost;
	public final int blockSize;
	public final int parallelization;
	
	public String base64IV;
	
	public ArmoredScryptResult(byte[] enc, byte[] salt, byte [] iv, int cpuMemoryCost, int blockSize, int parallelization) {
		super(enc, salt);
		try {
			this.base64IV = Base64.encodeBytes(iv, Base64.URL_SAFE);
		}catch(Exception x){
			throw new RuntimeException(x);
		}
		this.cpuMemoryCost = cpuMemoryCost;
		this.blockSize = blockSize;
		this.parallelization = parallelization;
	}

	public ArmoredScryptResult(String base64Enc, String base64Salt, String base64IV, int cpuMemoryCost, int blockSize, int parallelization) {
		super(base64Enc, base64Salt);
		this.base64IV = base64IV;
		this.cpuMemoryCost = cpuMemoryCost;
		this.blockSize = blockSize;
		this.parallelization = parallelization;
	}
	
	public ArmoredScryptResult(String version, Date createdOn, String base64Enc, String base64Salt, String base64IV, int cpuMemoryCost, int blockSize, int parallelization) {
		super(version, createdOn, base64Enc, base64Salt);
		this.base64IV = base64IV;
		this.cpuMemoryCost = cpuMemoryCost;
		this.blockSize = blockSize;
		this.parallelization = parallelization;
	}
	
	public PBEParams generateParams(Password password) {
		PBEParams params = new PBEParams(PBEAlg.SCRYPT);
		params.setPassword(password);
		params.setSalt(super.getSaltWrapper());
		params.setIv(this.getIVWrapper());
		params.setBlockSize_r(this.blockSize);
		params.setCpuMemoryCost_N(this.cpuMemoryCost);
		params.setParallelization_p(this.parallelization);
		return params;
	}
	
	public byte [] getIVBytes(){
		try {
			return Base64.decode(base64IV, Base64.URL_SAFE);
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}
	
	public SensitiveBytes getIVWrapper() {
		return new SensitiveBytes(getIVBytes());
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result + blockSize;
		result = prime * result + cpuMemoryCost;
		result = prime * result + parallelization;
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (!super.equals(obj))
			return false;
		if (getClass() != obj.getClass())
			return false;
		ArmoredScryptResult other = (ArmoredScryptResult) obj;
		if (blockSize != other.blockSize)
			return false;
		if (cpuMemoryCost != other.cpuMemoryCost)
			return false;
		if (parallelization != other.parallelization)
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "ArmoredScryptResult [cpuMemoryCost=" + cpuMemoryCost
				+ ", blockSize=" + blockSize + ", parallelization="
				+ parallelization + ", base64Enc=" + base64Enc
				+ ", base64Salt=" + base64Salt + "]";
	}

}
