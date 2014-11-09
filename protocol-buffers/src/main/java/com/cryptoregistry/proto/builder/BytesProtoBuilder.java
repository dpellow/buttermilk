/*
 *  This file is part of Buttermilk
 *  Copyright 2011-2014 David R. Smith All Rights Reserved.
 *
 */
package com.cryptoregistry.proto.builder;

import com.cryptoregistry.passwords.SensitiveBytes;
import com.cryptoregistry.protos.Buttermilk.BytesProto;
import com.cryptoregistry.symmetric.mt.Segment;
import com.google.protobuf.ByteString;

/**
 * Make a proto from a Segment
 * 
 * @author Dave
 *
 */
public class BytesProtoBuilder {

	final byte [] data;
	
	public BytesProtoBuilder(Segment segment) {
		this.data = segment.getOutput();
	}
	
	public BytesProtoBuilder(SensitiveBytes sb) {
		this.data = sb.getData();
	}
	
	public BytesProtoBuilder(byte[]data) {
		this.data = data;
	}
	
	public BytesProto build() {
		
		BytesProto.Builder builder = BytesProto.newBuilder();
		builder.setData(ByteString.copyFrom(data));
		
		return builder.build();
	}
}
