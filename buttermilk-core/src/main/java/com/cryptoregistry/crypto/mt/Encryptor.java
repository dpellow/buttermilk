/*
 *  This file is part of Buttermilk
 *  Copyright 2011-2014 David R. Smith All Rights Reserved.
 *
 */
package com.cryptoregistry.crypto.mt;

import java.util.concurrent.Callable;

/**
 * This is like a Runnable wrapper for an encryption operation
 * 
 * @author Dave
 *
 */

public class Encryptor extends AESGCM_MT implements Callable<Segment> {
	
	final Segment segment;

	public Encryptor(byte[] key, byte[] iv, Segment segment) {
		super(key, iv);
		this.segment = segment;
	}

	@Override
	public Segment call() throws Exception {
		segment.setOutput(super.encrypt(segment.getInput()));
		return segment;
	}

}
