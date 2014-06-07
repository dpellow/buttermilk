package com.cryptoregistry;

/**
 * These are names and codes for the types of cryptographic keys we know how to generate
 * 
 * @author Dave
 *
 */
public enum KeyGenerationAlgorithm {
	Curve25519('C'),EC('E'),RSA('R'),DSA('D');
	
	public final char code;

	private KeyGenerationAlgorithm(char code) {
		this.code = code;
	}
	
}
