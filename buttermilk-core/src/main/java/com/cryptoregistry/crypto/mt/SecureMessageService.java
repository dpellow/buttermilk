package com.cryptoregistry.crypto.mt;

public class SecureMessageService extends AESService {

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
		super.runEncryptTasks(msg.getSegments());
	}
	
	public void decrypt(){
		super.runDecryptTasks(msg.getSegments());
	}
}
