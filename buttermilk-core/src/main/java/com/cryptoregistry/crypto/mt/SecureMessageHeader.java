package com.cryptoregistry.crypto.mt;

import java.nio.charset.Charset;

import com.cryptoregistry.crypto.mt.SecureMessage.InputType;

public class SecureMessageHeader {

	public final InputType type; // original input, used for reconstruction of the payload
	public final Charset charset; // if input is String, what is the original Charset? Otherwise null
	public final byte [] iv; // our AES IV
	
	public SecureMessageHeader(InputType type, Charset charset, byte[] iv) {
		super();
		this.type = type;
		this.charset = charset;
		this.iv = iv;
	}
	
	public boolean needsCharset() {
		return type == InputType.STRING;
	}

}
