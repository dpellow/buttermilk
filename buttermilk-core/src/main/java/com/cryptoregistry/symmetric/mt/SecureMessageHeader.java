/*
 *  This file is part of Buttermilk
 *  Copyright 2011-2015 David R. Smith All Rights Reserved.
 *
 */
package com.cryptoregistry.symmetric.mt;

public class SecureMessageHeader {

	public final byte [] iv; // our AES IV
	
	public SecureMessageHeader(byte[] iv) {
		super();
		this.iv = iv;
	}
}
