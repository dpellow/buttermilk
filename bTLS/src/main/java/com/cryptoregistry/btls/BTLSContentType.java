package com.cryptoregistry.btls;

public enum BTLSContentType {
	
	Alert(21), Handshake(22), Application(23), Heartbeat(24), Close_Session(25);
	
	private final byte code;
	
	private BTLSContentType(int code){
		this.code = (byte) code;
	}

	public byte getCode() {
		return code;
	}
	
}
