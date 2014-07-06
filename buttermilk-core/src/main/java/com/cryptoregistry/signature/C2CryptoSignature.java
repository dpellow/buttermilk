package com.cryptoregistry.signature;

import java.io.IOException;
import java.io.Writer;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import com.cryptoregistry.SignatureAlgorithm;
import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.core.JsonGenerator;

public class C2CryptoSignature extends CryptoSignature {

	private static final long serialVersionUID = 1L;
	
	public final C2Signature signature;

	public C2CryptoSignature(SignatureMetadata metadata, C2Signature sig) {
		super(metadata);
		this.signature=sig;
	}
	
	public C2CryptoSignature(SignatureMetadata metadata, List<String> dataRefs, C2Signature sig) {
		super(metadata,dataRefs);
		this.signature=sig;
	}
	
	public C2CryptoSignature(String signedWith, String signedBy, C2Signature sig) {
		super(new SignatureMetadata(
				UUID.randomUUID().toString(),
				new Date(),
				SignatureAlgorithm.ECKCDSA,
				"SHA-256",
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

	@Override
	public void formatSignaturePrimitivesJSON(JsonGenerator g, Writer writer) throws JsonGenerationException, IOException {
		g.writeStringField("v", signature.v.data);
		g.writeStringField("r", signature.r.data);
	}

	@Override
	public SignatureBytes signatureBytes() {
		return signature;
	}

}
