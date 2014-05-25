/*
 *  This file is part of Buttermilk
 *  Copyright 2011-2014 David R. Smith All Rights Reserved.
 *
 */
package com.cryptoregistry.curve25519.key;

import java.io.IOException;
import java.util.UUID;

import net.iharder.Base64;

/**
 * Base class to wrap bytes used as keys by the Curve25519 class
 * 
 * @author Dave
 * @see CryptoFactory
 */
public class Key {

	// UUID in String representation - should never be null
	protected final String handle;
	
	// actual key bytes
	protected final byte [] bytes;
	
	// status of the key
	protected boolean alive;
	
	public Key(byte [] bytes) {
		this.bytes = bytes;
		handle = UUID.randomUUID().toString();
	}

	public Key(String handle, byte[] bytes) {
		super();
		this.handle = handle;
		this.bytes = bytes;
		if(handle == null) throw new RuntimeException("Handle can never be null");
	}

	public byte[] getBytes() {
		if(!alive) throw new DeadKeyException();
		return bytes;
	}
	
	public String getBase64Encoding() {
		if(!alive) throw new DeadKeyException();
		return Base64.encodeBytes(bytes);
	}
	
	public String getBase64UrlEncoding() {
		if(!alive) throw new DeadKeyException();
		try {
			return Base64.encodeBytes(bytes, Base64.URL_SAFE);
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}
	
	public void selfDestruct() {
		for(int i = 0;i<bytes.length;i++){
			bytes[i] = '\0';
		}
		alive = false;
	}

	public boolean isAlive() {
		return alive;
	}

	@Override
	public String toString() {
		return handle;
	}

}
