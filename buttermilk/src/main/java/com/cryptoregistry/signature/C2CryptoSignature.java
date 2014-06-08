package com.cryptoregistry.signature;

import java.util.Date;
import java.util.UUID;

import com.cryptoregistry.SignatureAlgorithm;

public class C2CryptoSignature extends CryptoSignature {

	private static final long serialVersionUID = 1L;
	
	public final C2Signature signature;

	public C2CryptoSignature(SignatureMetadata metadata, C2Signature sig) {
		super(metadata);
		this.signature=sig;
	}
	
	public C2CryptoSignature(String signedWith, String signedBy, C2Signature sig) {
		super(new SignatureMetadata(
				UUID.randomUUID().toString(),
				new Date(),
				SignatureAlgorithm.ECKCDSA,
				signedWith,
				signedBy));
		this.signature=sig;
	}

	public C2Signature getSignature() {
		return signature;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result
				+ ((signature == null) ? 0 : signature.hashCode());
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
		C2CryptoSignature other = (C2CryptoSignature) obj;
		if (signature == null) {
			if (other.signature != null)
				return false;
		} else if (!signature.equals(other.signature))
			return false;
		return true;
	}
}
