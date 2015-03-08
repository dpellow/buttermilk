/*
 *  This file is part of Buttermilk
 *  Copyright 2011-2014 David R. Smith All Rights Reserved.
 *
 */
package com.cryptoregistry;

/**
 * These are names and codes for the types of cryptographic keys Buttermilk knows how to generate
 * 
 * @author Dave
 *
 */
public enum KeyGenerationAlgorithm {
	Symmetric('S'), 
	Curve25519('C'), 
	EC('E'), 
	RSA('R'), 
	DSA('D'), 
	NTRU('N');
	
	public final char code;

	private KeyGenerationAlgorithm(char code) {
		this.code = code;
	}
	
	public static KeyGenerationAlgorithm [] usableForSignature(){
		KeyGenerationAlgorithm [] a = new KeyGenerationAlgorithm[3];
		a[0]=Curve25519;
		a[1]=EC;
		a[2]=RSA;
		return a;
	}
	
}
