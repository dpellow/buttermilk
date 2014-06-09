package com.cryptoregistry.signature;

import com.cryptoregistry.util.ArmoredString;

public class RSASignature {

	public final ArmoredString signature;

	public RSASignature(ArmoredString signature) {
		super();
		this.signature = signature;
	}
	
	public RSASignature(byte [] signature) {
		super();
		this.signature = new ArmoredString(signature);
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((signature == null) ? 0 : signature.hashCode());
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
		RSASignature other = (RSASignature) obj;
		if (signature == null) {
			if (other.signature != null)
				return false;
		} else if (!signature.equals(other.signature))
			return false;
		return true;
	}

}
