package com.cryptoregistry.symmetric.mt;

public class SecureMessageHeader {

	public final byte [] iv; // our AES IV
	
	public SecureMessageHeader(byte[] iv) {
		super();
		this.iv = iv;
	}
}
