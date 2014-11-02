/*
 *  This file is part of Buttermilk
 *  Copyright 2011-2014 David R. Smith All Rights Reserved.
 *
 */
package com.cryptoregistry.proto.builder;

import com.cryptoregistry.crypto.mt.SecureMessage;
import com.cryptoregistry.crypto.mt.Segment;
import com.cryptoregistry.protos.Buttermilk.BytesProto;
import com.cryptoregistry.protos.Buttermilk.SecureBytesProto;
import com.google.protobuf.ByteString;

/**
 * builder for SecureMessageProtos
 * 
 * @author Dave
 *
 */
public class SecureBytesProtoBuilder {

	final SecureMessage message;
	
	public SecureBytesProtoBuilder(SecureMessage message) {
		this.message = message;
	}

	public SecureBytesProto build() {
		SecureBytesProto.Builder builder = SecureBytesProto.newBuilder();
		builder.setIv(ByteString.copyFrom(message.getHeader().iv));
		for(Segment s: message.getSegments()){
			BytesProto.Builder sb = BytesProto.newBuilder();
			sb.setData(ByteString.copyFrom(s.getOutput()));
			builder.addSegments(sb.build());
		}
		return builder.build();
	}
}
