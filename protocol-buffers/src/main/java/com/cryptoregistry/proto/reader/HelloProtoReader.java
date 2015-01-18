/*
 *  This file is part of Buttermilk
 *  Copyright 2011-2014 David R. Smith All Rights Reserved.
 *
 */
package com.cryptoregistry.proto.reader;

import com.cryptoregistry.protos.Buttermilk.HelloProto;

public class HelloProtoReader {

	final HelloProto proto;

	public HelloProtoReader(HelloProto proto) {
		super();
		this.proto = proto;
	}

	public Hello read() {
		return new Hello(proto.getRegistrationHandle(), proto.getKeyHandle());
	}
}
