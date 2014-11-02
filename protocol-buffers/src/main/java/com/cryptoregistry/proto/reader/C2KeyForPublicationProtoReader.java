/*
 *  This file is part of Buttermilk
 *  Copyright 2011-2014 David R. Smith All Rights Reserved.
 *
 */
package com.cryptoregistry.proto.reader;

import com.cryptoregistry.c2.key.C2KeyMetadata;
import com.cryptoregistry.c2.key.Curve25519KeyForPublication;
import com.cryptoregistry.c2.key.PublicKey;
import com.cryptoregistry.protos.Buttermilk.C2KeyForPublicationProto;


public class C2KeyForPublicationProtoReader {

	final C2KeyForPublicationProto proto;

	public C2KeyForPublicationProtoReader(C2KeyForPublicationProto proto) {
		super();
		this.proto = proto;
	}

	public Curve25519KeyForPublication read() {
		C2KeyMetadata meta = (C2KeyMetadata) new KeyMetadataProtoReader(
				proto.getMeta()).read();
		
		PublicKey pk = new PublicKey(proto.getPublicKey().toByteArray());
		
		Curve25519KeyForPublication fp = new Curve25519KeyForPublication(meta,pk);
		return fp;
	}
}
