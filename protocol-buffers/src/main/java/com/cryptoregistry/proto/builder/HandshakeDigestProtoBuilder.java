/*
 *  This file is part of Buttermilk
 *  Copyright 2011-2014 David R. Smith All Rights Reserved.
 *
 */
package com.cryptoregistry.proto.builder;

import com.cryptoregistry.protos.Buttermilk.HandshakeDigestProto;
import com.google.protobuf.ByteString;

/**
 * 
 * @author Dave
 *
 */
public class HandshakeDigestProtoBuilder {

	final byte [] data;
	
	public HandshakeDigestProtoBuilder(byte[]data) {
		this.data = data;
	}
	
	public HandshakeDigestProto build() {
		
		HandshakeDigestProto.Builder builder = HandshakeDigestProto.newBuilder();
		builder.setDigest(ByteString.copyFrom(data));
		
		return builder.build();
	}
}
