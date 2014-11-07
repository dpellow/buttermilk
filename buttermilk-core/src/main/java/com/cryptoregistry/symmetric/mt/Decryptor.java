/*
 *  This file is part of Buttermilk
 *  Copyright 2011-2014 David R. Smith All Rights Reserved.
 *
 */
package com.cryptoregistry.symmetric.mt;

import java.util.concurrent.Callable;

/**
 * This is like a Runnable wrapper for a decryption operation
 * 
 * @author Dave
 *
 */
public class Decryptor extends AESGCM_MT implements Callable<Segment> {
	
	final Segment segment;

	public Decryptor(byte[] key, byte[] iv, Segment segment) {
		super(key, iv);
		this.segment = segment;
	}

	@Override
	public Segment call() throws Exception {
		segment.setOutput(super.decrypt(segment.getInput()));
		segment.freeInput();
		return segment;
	}

}
