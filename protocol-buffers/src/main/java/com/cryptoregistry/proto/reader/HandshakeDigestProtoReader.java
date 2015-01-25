/*
 *  This file is part of Buttermilk
 *  Copyright 2011-2014 David R. Smith All Rights Reserved.
 *
 */
package com.cryptoregistry.proto.reader;

import com.cryptoregistry.protos.Buttermilk.HandshakeDigestProto;

public class HandshakeDigestProtoReader {

	final HandshakeDigestProto proto;

	public HandshakeDigestProtoReader(HandshakeDigestProto proto) {
		super();
		this.proto = proto;
	}

	public byte [] read() {
		return proto.getDigest().toByteArray();
	}
}
