/*
 *  This file is part of Buttermilk
 *  Copyright 2011-2014 David R. Smith All Rights Reserved.
 *
 */
package com.cryptoregistry.proto.builder;

import com.cryptoregistry.protos.Buttermilk.SessionDetailsProto;
import com.google.protobuf.ByteString;

/**
 * Make a proto buffer suitable to write the Session Details as a Proto
 * 
 * @author Dave
 *
 */
public class SessionDetailsProtoBuilder {

	final String sessionId;
	final byte [] sessionKey;
	final String sessionSymmetricAlg;
	final byte [] iv; // can be null for certain algorithms

	public SessionDetailsProtoBuilder(String sessionId, byte[] sessionKey,
			String sessionSymmetricAlg, byte[] iv) {
		super();
		this.sessionId = sessionId;
		this.sessionKey = sessionKey;
		this.sessionSymmetricAlg = sessionSymmetricAlg;
		this.iv = iv;
	}



	public SessionDetailsProto build() {
		
		SessionDetailsProto proto = SessionDetailsProto.newBuilder()
				.setSessionId(sessionId)
				.setSessionKey(ByteString.copyFrom(sessionKey))
				.setSessionSymmetricAlg(sessionSymmetricAlg)
				.setIv(ByteString.copyFrom(iv))
				.build();
		
		return proto;
		
	}
}
