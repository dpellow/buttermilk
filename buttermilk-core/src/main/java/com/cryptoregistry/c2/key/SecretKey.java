/*
 *  This file is part of Buttermilk
 *  Copyright 2011-2014 David R. Smith All Rights Reserved.
 *
 */
package com.cryptoregistry.c2.key;

import x.org.bouncycastle.crypto.Digest;
import x.org.bouncycastle.crypto.digests.SHA256Digest;

/**
 * Wrapper for the bytes produced by the C25519 key agreement algorithm 
 * (value should be run through a suitable digest prior to use as key, getSHA256Digest() does this)
 * 
 * @author Dave
 *
 */
public class SecretKey extends Key {

	public SecretKey(byte[] bytes) {
		super(bytes);
	}
	
	SecretKey(byte[] bytes,boolean alive) {
		super(bytes,alive);
	}
	
	public byte [] getDigest(Digest digest) {
		digest.update(bytes, 0, bytes.length);
		byte[]  digestBytes = new byte[digest.getDigestSize()];
	    digest.doFinal(digestBytes, 0);
	    return digestBytes;
	}
	
	public byte [] getSHA256Digest(){
		return getDigest(new SHA256Digest());
	}

}
