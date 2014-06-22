/*
 *  This file is part of Buttermilk
 *  Copyright 2011-2014 David R. Smith All Rights Reserved.
 *
 */
package com.cryptoregistry.c2.key;

import java.io.IOException;

import net.iharder.Base64;

/**
 * Base class to wrap bytes used as keys by the Curve25519 class
 * 
 * @author Dave
 * @see CryptoFactory
 */
public class Key {
	
	// actual key bytes
	protected final byte [] bytes;
	
	// status of the key
	protected boolean alive = true;

	public Key(byte [] bytes) {
		this.bytes = bytes;
	}
	
	protected Key(byte [] bytes, boolean alive) {
		this.bytes = bytes;
		this.alive = alive;
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
	
	public int length() {
		return bytes.length;
	}
	
	public Key clone() {
		int length = this.bytes.length;
		byte [] b = new byte[length];
		System.arraycopy(bytes, 0, b, 0, length);
		boolean isAlive = this.alive;
		if(this instanceof SigningPrivateKey){
			return new SigningPrivateKey(b,isAlive);
		}else if(this instanceof AgreementPrivateKey){
			return new AgreementPrivateKey(b,isAlive);
		}else if(this instanceof PublicKey){
			return new PublicKey(b,isAlive);
		}else if(this instanceof SecretKey){
			return new PublicKey(b,isAlive);
		}else if(this instanceof PrivateKey){
			return new PrivateKey(b,isAlive);
		}else{
			return new Key(b,isAlive);
		}
	}


}
