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

	public final PublicKey publicKey;
	public final C2KeyManagement management;

	public Curve25519KeyForPublication(PublicKey publicKey) {
		super();
		this.publicKey = publicKey;
		management = C2KeyManagement.createForPublication();
	} 
	
	public Curve25519KeyForPublication(C2KeyManagement management, PublicKey publicKey) {
		super();
		this.publicKey = publicKey;
		this.management = management;
	} 

}
