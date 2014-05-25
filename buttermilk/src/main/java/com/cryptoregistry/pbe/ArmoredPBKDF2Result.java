package com.cryptoregistry.pbe;

public class ArmoredPBKDF2Result extends ArmoredPBEResult {

	public final int iterations;
	
	public ArmoredPBKDF2Result(byte[] enc, byte[] salt, int iterations) {
		super(enc, salt);
		this.iterations = iterations;
	}

	public ArmoredPBKDF2Result(String base64Enc, String base64Salt, int iterations) {
		super(base64Enc, base64Salt);
		this.iterations = iterations;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result + iterations;
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
		ArmoredPBKDF2Result other = (ArmoredPBKDF2Result) obj;
		if (iterations != other.iterations)
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "ArmoredPBKDF2Result [iterations=" + iterations + ", base64Enc="
				+ base64Enc + ", base64Salt=" + base64Salt + "]";
	}

}
