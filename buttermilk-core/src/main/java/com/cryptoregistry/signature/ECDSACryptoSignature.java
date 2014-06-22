package com.cryptoregistry.signature;

import java.io.IOException;
import java.io.Writer;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import com.cryptoregistry.SignatureAlgorithm;
import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.core.JsonGenerator;

public class ECDSACryptoSignature extends CryptoSignature {

	private static final long serialVersionUID = 1L;
	
	public final ECDSASignature signature;

	public ECDSACryptoSignature(SignatureMetadata metadata, ECDSASignature sig) {
		super(metadata);
		this.signature=sig;
	}
	
	public ECDSACryptoSignature(SignatureMetadata metadata, List<String> dataRefs, ECDSASignature sig) {
		super(metadata,dataRefs);
		this.signature=sig;
	}
	
	public ECDSACryptoSignature(String signedWith, String signedBy, ECDSASignature sig) {
		super(new SignatureMetadata(
				UUID.randomUUID().toString(),
				new Date(),
				SignatureAlgorithm.ECDSA,
				"SHA-256",
				signedWith,
				signedBy));
		this.signature=sig;
	}

	public ECDSASignature getSignature() {
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
		ECDSACryptoSignature other = (ECDSACryptoSignature) obj;
		if (signature == null) {
			if (other.signature != null)
				return false;
		} else if (!signature.equals(other.signature))
			return false;
		return true;
	}


	@Override
	public void formatSignaturePrimitivesJSON(JsonGenerator g, Writer writer)
			throws JsonGenerationException, IOException {
		g.writeStringField("r", signature.r.toString(16));
		g.writeStringField("s", signature.s.toString(16));
		
	}
}
