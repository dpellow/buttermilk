package com.cryptoregistry.proto.frame.btls;

public enum HandshakeCode {
	CLIENT_HELLO(100), SEND_KEY(101), SERVER_HELLO(102), CANNOT_SEND_KEY(103), 
	FAILED_VALIDATION(104), FAILED_HANDSHAKE(105), CLIENT_READY(106), SERVER_READY(107); 
	
	private HandshakeCode(int code){
		this.code = code;
	}
	
	public final int code;
}
