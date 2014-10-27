/*
 *  This file is part of Buttermilk
 *  Copyright 2011-2014 David R. Smith All Rights Reserved.
 *
 */
package com.cryptoregistry.proto.reader;

import com.cryptoregistry.c2.key.C2KeyMetadata;
import com.cryptoregistry.c2.key.Curve25519KeyContents;
import com.cryptoregistry.c2.key.Curve25519KeyForPublication;
import com.cryptoregistry.c2.key.PublicKey;
import com.cryptoregistry.protos.Buttermilk.C2KeyContentsProto;
import com.cryptoregistry.protos.Buttermilk.HelloProto;

public class HelloProtoReader {

	final HelloProto proto;

	public HelloProtoReader(HelloProto proto) {
		super();
		this.proto = proto;
	}

	public Curve25519KeyForPublication read() {
		C2KeyContentsProto c2Proto = proto.getC2KeyContents();
		C2KeyMetadata meta = (C2KeyMetadata) new KeyMetadataProtoReader(
				c2Proto.getMeta()).read();
		
		PublicKey pk = new PublicKey(c2Proto.getPublicKey().toByteArray());
		Curve25519KeyContents fp = new Curve25519KeyContents(meta,pk,null,null);
		return fp;
	}
}
