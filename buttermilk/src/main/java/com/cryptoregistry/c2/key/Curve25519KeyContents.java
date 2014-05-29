/*
 *  This file is part of Buttermilk
 *  Copyright 2011-2014 David R. Smith All Rights Reserved.
 *
 */
package com.cryptoregistry.c2.key;

/**
 * Wrapper for output from the Curve25519 key generation method
 * 
 * @author Dave
 *
 */
public class Curve25519KeyContents extends Curve25519KeyForPublication {

	public final SigningPrivateKey signingPrivateKey;
	public final AgreementPrivateKey agreementPrivateKey;
	
	public Curve25519KeyContents(String handle, PublicKey key0, SigningPrivateKey key1, AgreementPrivateKey key2) {
		super(handle,key0);
		signingPrivateKey = key1;
		agreementPrivateKey = key2;
	}

}
