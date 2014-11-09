/*
 *  This file is part of Buttermilk
 *  Copyright 2011-2014 David R. Smith All Rights Reserved.
 *
 */
package com.cryptoregistry.symmetric.mt;

import java.util.concurrent.Callable;

/**
 * This is like a Runnable wrapper for an encryption operation
 * 
 * Note: based on my reading GCM requires an ephemeral (one-use) key and iv to retain the security properties.  
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
		segment.freeInput();
		return segment;
	}

}
