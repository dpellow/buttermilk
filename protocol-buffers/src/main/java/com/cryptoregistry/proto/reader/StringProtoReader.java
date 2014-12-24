/*
 *  This file is part of Buttermilk
 *  Copyright 2011-2014 David R. Smith All Rights Reserved.
 *
 */
package com.cryptoregistry.proto.reader;

import java.io.UnsupportedEncodingException;

import com.cryptoregistry.protos.Buttermilk.StringProto;

public class StringProtoReader {

	final StringProto proto;

	public StringProtoReader(StringProto proto) {
		super();
		this.proto = proto;
	}

	public String read() {
		try {
			return proto.getDataBytes().toString("UTF-8");
		} catch (UnsupportedEncodingException e) {
			throw new RuntimeException("Bad encoding");
		}
	}
}
