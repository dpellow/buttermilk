/*
 *  This file is part of Buttermilk
 *  Copyright 2011-2014 David R. Smith All Rights Reserved.
 *
 */
package com.cryptoregistry.c2.key;

import java.util.Arrays;

public class SigningPrivateKey extends PrivateKey {

	public SigningPrivateKey(byte[] bytes) {
		super(bytes);
	}

	SigningPrivateKey(byte[] bytes,boolean alive) {
		super(bytes,alive);
	}
	
	public boolean equals(Object obj){
		if (this == obj) return true;
		if (obj == null) return false;
		if (getClass() != obj.getClass()) return false;
		SigningPrivateKey k = (SigningPrivateKey) obj;
		if((this.alive != k.alive)) return false; 
		return Arrays.equals(this.getBytes(),k.getBytes());
	}
}
