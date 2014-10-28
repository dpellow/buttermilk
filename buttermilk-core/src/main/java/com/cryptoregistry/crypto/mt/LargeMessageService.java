package com.cryptoregistry.crypto.mt;

public class LargeMessageService extends AESService {

	LargeMessage msg;
	
	public LargeMessageService(byte[] key, LargeMessage message) {
		super(key, message.getHeader().iv);
		msg = message;
	}

	public LargeMessageService(int poolSize, byte[] key, LargeMessage message) {
		super(poolSize, key, message.getHeader().iv);
		msg = message;
	}
	
	public void encrypt(){
		super.runEncryptTasks(msg.getSegments());
	}
	
	public void decrypt(){
		super.runDecryptTasks(msg.getSegments());
	}
}
