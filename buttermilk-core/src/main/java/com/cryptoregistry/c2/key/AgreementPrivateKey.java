/*
 *  This file is part of Buttermilk
 *  Copyright 2011-2014 David R. Smith All Rights Reserved.
 *
 */
package com.cryptoregistry.c2.key;

import java.util.Arrays;

/**
 * Type for holding a KeyAgreement key.
 * 
 * @author Dave
 *
 */
public class AgreementPrivateKey extends PrivateKey {

	public AgreementPrivateKey(byte[] bytes) {
		super(bytes);
	}
	
	AgreementPrivateKey(byte[] bytes,boolean alive) {
		super(bytes,alive);
	}
	
	public boolean equals(Object obj){
		if (this == obj) return true;
		if (obj == null) return false;
		if (getClass() != obj.getClass()) return false;
		AgreementPrivateKey k = (AgreementPrivateKey) obj;
		if((this.alive != k.alive)) return false; 
		return Arrays.equals(this.getBytes(),k.getBytes());
	}
}
