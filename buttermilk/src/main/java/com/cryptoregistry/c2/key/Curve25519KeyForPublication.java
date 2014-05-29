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
public class Curve25519KeyForPublication {

	public final String handle;
	public final PublicKey publicKey; 
	
	public Curve25519KeyForPublication(String handle, PublicKey key0) {
		this.handle = handle;
		publicKey = key0;
	}

}
