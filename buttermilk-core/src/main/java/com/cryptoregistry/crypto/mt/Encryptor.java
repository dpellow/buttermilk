package com.cryptoregistry.crypto.mt;

import java.util.concurrent.Callable;

public class Encryptor extends AESGCM_MT implements Callable<Segment> {
	
	final Segment segment;

	public Encryptor(byte[] key, byte[] iv, Segment segment) {
		super(key, iv);
		this.segment = segment;
	}

	@Override
	public Segment call() throws Exception {
		segment.output = super.encrypt(segment.input);
		return segment;
	}

}
