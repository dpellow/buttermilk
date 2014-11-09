/*
 *  This file is part of Buttermilk
 *  Copyright 2011-2014 David R. Smith All Rights Reserved.
 *
 */
package com.cryptoregistry.symmetric.mt;

/**
 * Base class for AES-GCM done with our multi-threaded executor
 *  
 */
import x.org.bouncycastle.crypto.params.KeyParameter;
import x.org.bouncycastle.crypto.params.ParametersWithIV;

class AESGCM_MT {

	private final byte [] key;
	private final byte [] iv;
	
	public AESGCM_MT(byte [] key, byte [] iv) {
		this.key = key;
		this.iv = iv;
	}
	
	public byte [] encrypt(byte [] input) {
		return genCipherData(GCMBlockCipher.aesgcm(true, buildKey()), input);
	}
	
	public byte [] decrypt(byte [] encrypted) {
		return genCipherData(GCMBlockCipher.aesgcm(false, buildKey()), encrypted);
	}
	
	private byte[] genCipherData(GCMBlockCipher cipher, byte[] data) {
	    int minSize = cipher.getOutputSize(data.length);
	    byte[] outBuf = new byte[minSize];
	    int length1 = cipher.processBytes(data, 0, data.length, outBuf, 0);
	    int length2 = cipher.doFinal(outBuf, length1);
	    int actualLength = length1 + length2;
	    byte[] result = new byte[actualLength];
	    System.arraycopy(outBuf, 0, result, 0, result.length);
	    return result;
	}
	
	private ParametersWithIV buildKey() {
		ParametersWithIV holder = new ParametersWithIV(
				new KeyParameter(key, 0, key.length), 
				iv, 
				0, 
				iv.length);
		return holder;
	}

}
