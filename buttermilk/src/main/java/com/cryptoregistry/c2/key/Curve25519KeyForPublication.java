/*
 *  This file is part of Buttermilk
 *  Copyright 2011-2014 David R. Smith All Rights Reserved.
 *
 */
package com.cryptoregistry.c2.key;

import java.util.Date;

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
public class Curve25519KeyForPublication  implements CryptoKeyMetadata,Verifier {

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
	
}
