package com.cryptoregistry.proto.reader;

public class Hello {

	public final String regHandle;
	public final String keyHandle;
	
	public Hello(String regHandle, String keyHandle) {
		super();
		this.regHandle = regHandle;
		this.keyHandle = keyHandle;
	}

	@Override
	public String toString() {
		return "Hello [regHandle=" + regHandle + ", keyHandle=" + keyHandle
				+ "]";
	}

}
