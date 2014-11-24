/*
 *  This file is part of Buttermilk
 *  Copyright 2011-2014 David R. Smith All Rights Reserved.
 *
 */
package com.cryptoregistry.proto.builder;

import com.cryptoregistry.protos.Buttermilk.HelloProto;

/**
 * Make a proto buffer 
 * 
 * @author Dave
 *
 */
public class HelloProtoBuilder {

	final String handle;
	final String keyHandle;
	
	public HelloProtoBuilder(String regHandle, String keyHandle) {
		super();
		this.handle = regHandle;
		this.keyHandle = keyHandle;
	}

	public HelloProto build() {
		
		HelloProto helloProto = HelloProto.newBuilder()
				.setRegistrationHandle(handle)
				.setKeyHandle(keyHandle)
				.build();
		
		return helloProto;
		
	}
}
