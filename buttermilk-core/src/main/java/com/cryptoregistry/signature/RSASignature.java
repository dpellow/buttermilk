package com.cryptoregistry.signature;

import com.cryptoregistry.util.ArmoredString;

public class RSASignature implements SignatureBytes {

	public final ArmoredString s;

	public RSASignature(ArmoredString signature) {
		super();
		this.s = signature;
	}
	
	public RSASignature(byte [] signature) {
		super();
		this.s = new ArmoredString(signature);
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((s == null) ? 0 : s.hashCode());
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
		if (s == null) {
			if (other.s != null)
				return false;
		} else if (!s.equals(other.s))
			return false;
		return true;
	}

	@Override
	public byte[] b1() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public byte[] b2() {
		// TODO Auto-generated method stub
		return null;
	}

}
