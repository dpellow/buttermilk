package com.cryptoregistry.curve25519.key;

/**
 * Wrapper for the bytes produced by the key agreement algorithm (value should be run through a suitable digest
 * prior to use as key)
 * 
 * @author Dave
 *
 */
public class SecretKey extends Key {

	public SecretKey(byte[] bytes) {
		super(bytes);
	}

}
