/*
 *  This file is part of Buttermilk
 *  Copyright 2011-2014 David R. Smith All Rights Reserved.
 *
 */
package com.cryptoregistry.curve25519.key;

/**
 * Wrapper for output from the Curve25519 key generation method
 * 
 * @author Dave
 *
 */
public class Curve25519KeyContents {

	public final String handle;
	public final PublicKey publicKey; 
	public final SigningPrivateKey signingPrivateKey;
	public final AgreementPrivateKey agreementPrivateKey;
	
	public Curve25519KeyContents(String handle, PublicKey key0, SigningPrivateKey key1, AgreementPrivateKey key2) {
		this.handle = handle;
		publicKey = key0;
		signingPrivateKey = key1;
		agreementPrivateKey = key2;
	}

}
