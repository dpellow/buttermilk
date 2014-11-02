package com.cryptoregistry.crypto.mt;

import com.cryptoregistry.util.StopWatch;

public class SecureMessageService extends AESService {
	
	public static final String enc_stopwatch = "com.cryptoregistry.crypto.mt.SecureMessageService.encrypt";
	public static final String dec_stopwatch = "com.cryptoregistry.crypto.mt.SecureMessageService.decrypt";

	SecureMessage msg;
	
	public SecureMessageService(byte[] key, SecureMessage message) {
		super(key, message.getHeader().iv);
		msg = message;
	}

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
