package com.cryptoregistry.signature;

/**
 * Currently the supported signatures have either one or two parts, each of which can be represented by a byte array
 * 
 * @author Dave
 *
 */
public interface SignatureBytes {

	public byte [] b1();
	public byte [] b2();
}
