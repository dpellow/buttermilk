/*
 *  This file is part of Buttermilk
 *  Copyright 2011-2014 David R. Smith All Rights Reserved.
 *
 */
package com.cryptoregistry.rsa;

import java.math.BigInteger;
import java.util.Date;

import com.cryptoregistry.CryptoKeyMetadata;
import com.cryptoregistry.KeyGenerationAlgorithm;
import com.cryptoregistry.Verifier;
import com.cryptoregistry.formats.KeyFormat;

import x.org.bouncycastle.crypto.params.RSAKeyParameters;

public class RSAKeyForPublication  implements CryptoKeyMetadata,Verifier {
	
	public final RSAKeyMetadata metadata;
	public final BigInteger  modulus;
	public final BigInteger  publicExponent;
	
	public RSAKeyForPublication(RSAKeyMetadata meta,
			BigInteger modulus, BigInteger publicExponent) {
		super();
		this.metadata = meta;
		this.modulus = modulus;
		this.publicExponent = publicExponent;
	}

	public final RSAKeyParameters getPublicKey() {
		return new RSAKeyParameters(false, modulus, publicExponent);
	}
	
	public final String toString() {
		return metadata.handle;
	}
	
	public String getHandle() {
		return metadata.getHandle();
	}
	
	public String getDistinguishedHandle() {
		return metadata.handle+"-"+metadata.format.mode.code;
	}

	public Date getCreatedOn() {
		return metadata.getCreatedOn();
	}

	public KeyFormat getFormat() {
		return metadata.getFormat();
	}

	@Override
	public KeyGenerationAlgorithm getKeyAlgorithm() {
		return KeyGenerationAlgorithm.RSA;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((metadata == null) ? 0 : metadata.hashCode());
		result = prime * result + ((modulus == null) ? 0 : modulus.hashCode());
		result = prime * result
				+ ((publicExponent == null) ? 0 : publicExponent.hashCode());
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
		RSAKeyForPublication other = (RSAKeyForPublication) obj;
		if (metadata == null) {
			if (other.metadata != null)
				return false;
		} else if (!metadata.equals(other.metadata))
			return false;
		if (modulus == null) {
			if (other.modulus != null)
				return false;
		} else if (!modulus.equals(other.modulus))
			return false;
		if (publicExponent == null) {
			if (other.publicExponent != null)
				return false;
		} else if (!publicExponent.equals(other.publicExponent))
			return false;
		return true;
	}
	
	
}
