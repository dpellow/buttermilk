/*
 *  This file is part of Buttermilk
 *  Copyright 2011-2014 David R. Smith All Rights Reserved.
 *
 */
package com.cryptoregistry.c2.key;

public class SigningPrivateKey extends PrivateKey {

	public SigningPrivateKey(byte[] bytes) {
		super(bytes);
	}

	SigningPrivateKey(byte[] bytes,boolean alive) {
		super(bytes,alive);
	}
}
