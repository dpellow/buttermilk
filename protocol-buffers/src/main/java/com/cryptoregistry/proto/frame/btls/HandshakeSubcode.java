package com.cryptoregistry.proto.frame.btls;

public enum HandshakeSubcode {
	CLIENT_HELLO(100), 
	SEND_KEY(101),
	SENDING_KEY(102),
	SERVER_HELLO(106), 
	CANNOT_SEND_KEY(107), 
	FAILED_VALIDATION(108), 
	FAILED_HANDSHAKE(109), 
	SUCCESSFUL_VALIDATION(110), 
	CLIENT_READY(111), 
	SERVER_READY(112); 
	
	private HandshakeSubcode(int code){
		this.code = code;
	}
	
	public final int code;
}
