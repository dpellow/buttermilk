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
import com.cryptoregistry.formats.KeyFormat;

import x.org.bouncycastle.crypto.params.RSAKeyParameters;

public class RSAKeyForPublication  implements CryptoKeyMetadata {
	
	public final RSAKeyMetadata management;
	public final BigInteger  modulus;
	public final BigInteger  publicExponent;
	
	public RSAKeyForPublication(RSAKeyMetadata management,
			BigInteger modulus, BigInteger publicExponent) {
		super();
		this.management = management;
		this.modulus = modulus;
		this.publicExponent = publicExponent;
	}

	public final RSAKeyParameters getPublicKey() {
		return new RSAKeyParameters(false, modulus, publicExponent);
	}
	
	public final String toString() {
		return management.handle;
	}
	
	public String getHandle() {
		return management.getHandle();
	}

	public Date getCreatedOn() {
		return management.getCreatedOn();
	}

	public KeyFormat getFormat() {
		return management.getFormat();
	}

	@Override
	public KeyGenerationAlgorithm getKeyAlgorithm() {
		return KeyGenerationAlgorithm.RSA;
	}
}
