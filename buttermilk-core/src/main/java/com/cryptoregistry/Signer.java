package com.cryptoregistry;

/**
 * Marker for classes which represent "private keys" or key contents
 * 
 * @author Dave
 *
 */
public interface Signer {

	public void scrubPassword();
}
