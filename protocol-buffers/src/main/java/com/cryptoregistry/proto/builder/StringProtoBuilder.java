/*
 *  This file is part of Buttermilk
 *  Copyright 2011-2014 David R. Smith All Rights Reserved.
 *
 */
package com.cryptoregistry.proto.builder;

import com.cryptoregistry.protos.Buttermilk.StringProto;

/**
 * Make a proto from a String
 * 
 * @author Dave
 *
 */
public class StringProtoBuilder {

	final String data;
	
	public StringProtoBuilder(String in) {
		this.data = in;
	}
	
	public StringProto build() {
		
		StringProto.Builder builder = StringProto.newBuilder();
		builder.setData(data);
		
		return builder.build();
	}
}
