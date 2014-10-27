/*
 *  This file is part of Buttermilk
 *  Copyright 2011-2014 David R. Smith All Rights Reserved.
 *
 */
package com.cryptoregistry.c2.key;

import java.util.Date;

import com.cryptoregistry.Signer;
import com.cryptoregistry.formats.KeyFormat;
import com.cryptoregistry.passwords.Password;
import com.cryptoregistry.pbe.PBEParams;


/**
 * Wrapper for output from the Curve25519 key generation method
 * 
 * @author Dave
 *
 */
public class Curve25519KeyContents extends Curve25519KeyForPublication implements Signer {

	public final SigningPrivateKey signingPrivateKey;
	public final AgreementPrivateKey agreementPrivateKey;
	
	/**
	 * Sets up for public formatting only
	 * @param key0
	 * @param key1
	 * @param key2
	 */
	public Curve25519KeyContents(PublicKey key0, SigningPrivateKey key1, AgreementPrivateKey key2) {
		super(C2KeyMetadata.createUnsecured(), key0);
		signingPrivateKey = key1;
		agreementPrivateKey = key2;
	}
	
	/**
	 * Set up for user choice format options
	 * @param management
	 * @param key0
	 * @param key1
	 * @param key2
	 */
	public Curve25519KeyContents(C2KeyMetadata management, PublicKey key0, SigningPrivateKey key1, AgreementPrivateKey key2) {
		super(management, key0);
		signingPrivateKey = key1;
		agreementPrivateKey = key2;
	}

	public Curve25519KeyContents clone() {
			C2KeyMetadata meta =  super.metadata.clone();
			PublicKey pubKey = (PublicKey) super.publicKey.clone();
			AgreementPrivateKey agree = (AgreementPrivateKey) this.agreementPrivateKey.clone();
			SigningPrivateKey signingKey = (SigningPrivateKey) this.signingPrivateKey.clone();
			Curve25519KeyContents c = new Curve25519KeyContents(meta, pubKey, signingKey, agree);
			return c;
	}
	
	public Curve25519KeyContents clone(KeyFormat format) {
		C2KeyMetadata meta = new C2KeyMetadata(this.getHandle(),new Date(this.getCreatedOn().getTime()),format);
		PublicKey pubKey = (PublicKey) super.publicKey.clone();
		AgreementPrivateKey agree = (AgreementPrivateKey) this.agreementPrivateKey.clone();
		SigningPrivateKey signingKey = (SigningPrivateKey) this.signingPrivateKey.clone();
		Curve25519KeyContents c = new Curve25519KeyContents(meta, pubKey, signingKey, agree);
		return c;
	}
	
	/**
	 * If a password is set in the KeyFormat, clean that out. This call can be made once we're done
	 * with the key materials in this cycle of use. 
	 */
	@Override
	public void scrubPassword() {
		PBEParams params = this.metadata.format.pbeParams;
		if(params != null) {
			Password password = params.getPassword();
			if(password != null && password.isAlive()) password.selfDestruct();
		}
	}
	
	public Curve25519KeyForPublication forPublication() {
		C2KeyMetadata meta = this.metadata.cloneForPublication();
		return new Curve25519KeyForPublication(meta,(PublicKey)publicKey.clone());
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime
				* result
				+ ((agreementPrivateKey == null) ? 0 : agreementPrivateKey
						.hashCode());
		result = prime
				* result
				+ ((signingPrivateKey == null) ? 0 : signingPrivateKey
						.hashCode());
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
		Curve25519KeyContents other = (Curve25519KeyContents) obj;
		if (agreementPrivateKey == null) {
			if (other.agreementPrivateKey != null)
				return false;
		} else if (!agreementPrivateKey.equals(other.agreementPrivateKey))
			return false;
		if (signingPrivateKey == null) {
			if (other.signingPrivateKey != null)
				return false;
		} else if (!signingPrivateKey.equals(other.signingPrivateKey))
			return false;
		return true;
	}

}
