/*
 *  This file is part of Buttermilk
 *  Copyright 2011-2014 David R. Smith All Rights Reserved.
 *
 */
package com.cryptoregistry.proto.builder;

import com.cryptoregistry.protos.Buttermilk.AuthenticatedStringProto;
import com.google.protobuf.ByteString;

/**
 * Make a proto from a String
 * 
 * @author Dave
 *
 */
public class AuthenticatedStringProtoBuilder {

	final String data;
	final byte [] hmac;
	
	public AuthenticatedStringProtoBuilder(String in, byte [] hmac) {
		this.data = in;
		this.hmac = hmac;
	}
	
	public AuthenticatedStringProto build() {
		
		AuthenticatedStringProto.Builder builder = AuthenticatedStringProto.newBuilder();
		builder.setData(data);
		builder.setHmac(ByteString.copyFrom(hmac));
		
		return builder.build();
	}
}
