package com.cryptoregistry.crypto.mt;

public class SecureMessageHeader {

	public final byte [] iv; // our AES IV
	
	public SecureMessageHeader(byte[] iv) {
		super();
		this.iv = iv;
	}
}
