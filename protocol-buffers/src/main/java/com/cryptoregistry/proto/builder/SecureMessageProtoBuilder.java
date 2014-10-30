/*
 *  This file is part of Buttermilk
 *  Copyright 2011-2014 David R. Smith All Rights Reserved.
 *
 */
package com.cryptoregistry.proto.builder;

import com.cryptoregistry.crypto.mt.SecureMessage;
import com.cryptoregistry.crypto.mt.Segment;
import com.cryptoregistry.proto.compat.InputTypeAdapter;
import com.cryptoregistry.protos.Buttermilk.SecureMessageProto;
import com.cryptoregistry.protos.Buttermilk.SegmentProto;
import com.google.protobuf.ByteString;

/**
 * builder for SecureMessageProtos
 * 
 * @author Dave
 *
 */
public class SecureMessageProtoBuilder {

	final SecureMessage message;
	
	public SecureMessageProtoBuilder(SecureMessage message) {
		this.message = message;
	}

	public SecureMessageProto build() {
		SecureMessageProto.Builder builder = SecureMessageProto.newBuilder();
		builder.setInputType(InputTypeAdapter.getInputTypeProtoFor(message.getHeader().type));
		if(message.getHeader().needsCharset()){
			builder.setCharset(message.getHeader().charset.name());
		}
		builder.setIv(ByteString.copyFrom(message.getHeader().iv));
		for(Segment s: message.getSegments()){
			SegmentProto.Builder sb = SegmentProto.newBuilder();
			sb.setData(ByteString.copyFrom(s.getOutput()));
			if(s.getIv() != null){
				sb.setIv(ByteString.copyFrom(s.getIv()));
			}
			builder.addSegments(sb.build());
		}
		return builder.build();
	}
}
