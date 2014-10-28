package com.cryptoregistry.crypto.mt;

import java.nio.charset.Charset;

import com.cryptoregistry.crypto.mt.LargeMessage.InputType;

public class LargeMessageHeader {

	final InputType type; // original input, used for reconstruction of the payload
	final Charset charset; // if input is String, what is the original Charset? Otherwise null
	final byte [] iv; // our AES IV
	
	public LargeMessageHeader(InputType type, Charset charset, byte[] iv) {
		super();
		this.type = type;
		this.charset = charset;
		this.iv = iv;
	}

}
