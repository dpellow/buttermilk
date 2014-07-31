/*
 *  This file is part of Buttermilk
 *  Copyright 2011-2014 David R. Smith All Rights Reserved.
 *
 */
package com.cryptoregistry.signature;

import java.io.IOException;
import java.io.Writer;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import com.cryptoregistry.SignatureAlgorithm;
import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.core.JsonGenerator;

public class RSACryptoSignature extends CryptoSignature {

	private static final long serialVersionUID = 1L;
	
	public final RSASignature signature;

	public RSACryptoSignature(SignatureMetadata metadata, RSASignature sig) {
		super(metadata);
		this.signature=sig;
	}
	
	public RSACryptoSignature(SignatureMetadata metadata, List<String> dataRefs, RSASignature sig) {
		super(metadata,dataRefs);
		this.signature=sig;
	}
	
	/**
	 * Assume SHA-256
	 * 
	 * @param signedWith
	 * @param signedBy
	 * @param sig
	 */
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


	@Override
	public void formatSignaturePrimitivesJSON(JsonGenerator g, Writer writer)
			throws JsonGenerationException, IOException {
		g.writeStringField("s", signature.s.toString());
	}
	
	@Override
	public SignatureBytes signatureBytes() {
		return signature;
	}
}
