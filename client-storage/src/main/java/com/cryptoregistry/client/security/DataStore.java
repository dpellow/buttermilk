package com.cryptoregistry.client.security;

import com.cryptoregistry.CryptoKey;
import com.cryptoregistry.CryptoKeyMetadata;

/**
 * Our key cache must implement this. Note that one data store may be used by many sockets, so it
 * needs to be implemented in a thread-safe manner
 * 
 * @author Dave
 *
 */
public interface DataStore {

	/**
	 *   given a handshake scenario with the specified remote metadata, find a key in our store we 
	 *   want use against it. there are many requirements here, for example with EC we would need 
	 *   a key with the same curve as the remote key for things to work. 
	 *   
	 *   the "generate" flag allows for server-side auto-generation of an appropriate key if we cannot
	 *   find a match. For example, if the remote key was of KeyGenerationAlgorithm type EC, with 
	 *   Curve P-256, then we would generate a similar type of key and add it to our store for immediate use
	 *   
	 *   The exception is thrown if the special conditions required for a match cannot be met
	 */
	
	
	public CryptoKey preferredHandshakeKey(CryptoKey remoteKey, boolean generate)
			throws SuitableMatchFailedException;
	
	// used during handshake, in essence with published keys
	public CryptoKey findKey(String regHandle, String keyHandle);
	public void saveKey(String regHandle, CryptoKey key);

}