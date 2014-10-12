package com.cryptoregistry.crypto.mt;

import java.util.concurrent.Callable;

public class Decryptor extends AESGCM_MT implements Callable<Segment> {
	
	final Segment segment;

	public Decryptor(byte[] key, byte[] iv, Segment segment) {
		super(key, iv);
		this.segment = segment;
	}

	@Override
	public Segment call() throws Exception {
		segment.output = super.decrypt(segment.input);
		return segment;
	}

}
