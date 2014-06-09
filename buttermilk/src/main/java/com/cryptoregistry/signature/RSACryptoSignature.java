package com.cryptoregistry.signature;

import java.util.Date;
import java.util.UUID;

import com.cryptoregistry.SignatureAlgorithm;

public class RSACryptoSignature extends CryptoSignature {

	private static final long serialVersionUID = 1L;
	
	public final RSASignature signature;

	public RSACryptoSignature(SignatureMetadata metadata, RSASignature sig) {
		super(metadata);
		this.signature=sig;
	}
	
	public RSACryptoSignature(String signedWith, String signedBy, RSASignature sig) {
		super(new SignatureMetadata(
				UUID.randomUUID().toString(),
				new Date(),
				SignatureAlgorithm.RSA,
				"SHA-256",
				signedWith,
				signedBy));
		this.signature=sig;
	}

	public RSASignature getSignature() {
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
		RSACryptoSignature other = (RSACryptoSignature) obj;
		if (signature == null) {
			if (other.signature != null)
				return false;
		} else if (!signature.equals(other.signature))
			return false;
		return true;
	}
	
	public SignatureData getSignatureData() {
		return signature;
	}
}
