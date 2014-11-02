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
import com.cryptoregistry.protos.Buttermilk.HelloAckProto;

public class HelloAckProtoReader {

	final HelloAckProto proto;
	byte[] rand32;

	public HelloAckProtoReader(HelloAckProto proto) {
		super();
		this.proto = proto;
	}

	public Curve25519KeyForPublication read() {
		C2KeyContentsProto c2Proto = proto.getC2KeyContents();
		C2KeyMetadata meta = (C2KeyMetadata) new KeyMetadataProtoReader(
				c2Proto.getMeta()).read();
		
		rand32 = proto.getRand32().toByteArray();
		
		PublicKey pk = new PublicKey(c2Proto.getPublicKey().toByteArray());
		Curve25519KeyContents fp = new Curve25519KeyContents(meta,pk,null,null);
		return fp;
	}

	public byte[] getRand32() {
		return rand32;
	}

}