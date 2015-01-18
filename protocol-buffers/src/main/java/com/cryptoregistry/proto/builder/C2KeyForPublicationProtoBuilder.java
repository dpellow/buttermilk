/*
 *  This file is part of Buttermilk
 *  Copyright 2011-2014 David R. Smith All Rights Reserved.
 *
 */
package com.cryptoregistry.proto.builder;

import com.cryptoregistry.c2.key.Curve25519KeyForPublication;
import com.cryptoregistry.protos.Buttermilk.C2KeyForPublicationProto;
import com.cryptoregistry.protos.Buttermilk.KeyMetadataProto;
import com.google.protobuf.ByteString;

/**
 * Make a proto buffer out of an C25519KeyForPublication instance - 
 * safe to use with contents, only sends public part
 * 
 * @author Dave
 *
 */
public class C2KeyForPublicationProtoBuilder {

	final Curve25519KeyForPublication keyContents;
	
	public C2KeyForPublicationProtoBuilder(Curve25519KeyForPublication keyContents) {
		this.keyContents = keyContents;
	}
	
	public C2KeyForPublicationProto build() {
		
		KeyMetadataProtoBuilder metaBuilder = new KeyMetadataProtoBuilder(keyContents.metadata);
		KeyMetadataProto metaProto = metaBuilder.build();
		
		C2KeyForPublicationProto c2Proto = C2KeyForPublicationProto.newBuilder()
				.setMeta(metaProto)
				.setPublicKey(ByteString.copyFrom(keyContents.publicKey.getBytes()))
				.build();
		
		return c2Proto;
		
	}
}
