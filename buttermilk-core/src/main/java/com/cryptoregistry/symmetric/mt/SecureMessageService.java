/*
 *  This file is part of Buttermilk
 *  Copyright 2011-2015 David R. Smith All Rights Reserved.
 *
 */
package com.cryptoregistry.symmetric.mt;

import com.cryptoregistry.util.StopWatch;

/**
 * Use only ephemeral key and iv here - never reuse them.
 * 
 * @author Dave
 *
 */
public class SecureMessageService extends AESService {
	
	public static final String enc_stopwatch = "com.cryptoregistry.crypto.mt.SecureMessageService.encrypt";
	public static final String dec_stopwatch = "com.cryptoregistry.crypto.mt.SecureMessageService.decrypt";

	SecureMessage msg;
	
	public SecureMessageService(byte[] key, SecureMessage message) {
		super(key, message.getHeader().iv);
		msg = message;
	}

	/**
	 * To prevent a message from being split up, set pool size to 1. This is recommended for messages
	 * below 1-5Mb in size (depending on hardware). 
	 * 
	 * @param poolSize
	 * @param key
	 * @param message
	 */
	public SecureMessageService(int poolSize, byte[] key, SecureMessage message) {
		super(poolSize, key, message.getHeader().iv);
		msg = message;
	}
	
	public void encrypt(){
		StopWatch.INSTANCE.start(enc_stopwatch);
		super.runEncryptTasks(msg.getSegments());
		StopWatch.INSTANCE.stop(enc_stopwatch);
	}
	
	public void decrypt(){
		StopWatch.INSTANCE.start(dec_stopwatch);
		super.runDecryptTasks(msg.getSegments());
		StopWatch.INSTANCE.stop(dec_stopwatch);
	}
}
