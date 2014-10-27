/*
 *  This file is part of Buttermilk
 *  Copyright 2011-2014 David R. Smith All Rights Reserved.
 *
 */
package com.cryptoregistry.c2.key;

import java.util.Date;

import com.cryptoregistry.CryptoKey;
import com.cryptoregistry.CryptoKeyMetadata;
import com.cryptoregistry.KeyGenerationAlgorithm;
import com.cryptoregistry.Verifier;
import com.cryptoregistry.formats.KeyFormat;

/**
 * Wrapper for output from the Curve25519 key generation method
 * 
 * @author Dave
 *
 */
public class Curve25519KeyForPublication  implements CryptoKey, Verifier {

	public final PublicKey publicKey;
	public final C2KeyMetadata metadata;

	public Curve25519KeyForPublication(PublicKey publicKey) {
		super();
		this.publicKey = publicKey;
		metadata = C2KeyMetadata.createForPublication();
	} 
	
	public Curve25519KeyForPublication(C2KeyMetadata meta, PublicKey publicKey) {
		super();
		this.publicKey = publicKey;
		this.metadata = meta;
	}
	
	public Curve25519KeyForPublication clone() {
		C2KeyMetadata meta =  metadata.clone();
		PublicKey pubKey = (PublicKey) publicKey.clone();
		Curve25519KeyForPublication  c = new Curve25519KeyForPublication (meta, pubKey);
		return c;
	}
	
	public Curve25519KeyForPublication clone(C2KeyMetadata meta) {
		PublicKey pubKey = (PublicKey) publicKey.clone();
		Curve25519KeyForPublication  c = new Curve25519KeyForPublication (meta, pubKey);
		return c;
	}

	public String getHandle() {
		return metadata.getHandle();
	}
	
	public String getDistinguishedHandle() {
		return metadata.handle+"-"+metadata.format.mode.code;
	}

	public KeyGenerationAlgorithm getKeyAlgorithm() {
		return metadata.getKeyAlgorithm();
	}

	public Date getCreatedOn() {
		return metadata.getCreatedOn();
	}

	public KeyFormat getFormat() {
		return metadata.getFormat();
	}
	
	public CryptoKeyMetadata getMetadata() {
		return metadata;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		
	// do not include metadata
	//	result = prime * result
	//			+ ((metadata == null) ? 0 : metadata.hashCode());
		
		result = prime * result
				+ ((publicKey == null) ? 0 : publicKey.hashCode());
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
		Curve25519KeyForPublication other = (Curve25519KeyForPublication) obj;
	
		//
		// do not include metadata in the equals method
		//
		
		if (publicKey == null) {
			if (other.publicKey != null)
				return false;
		} else if (!publicKey.equals(other.publicKey))
			return false;
		return true;
	}
}
