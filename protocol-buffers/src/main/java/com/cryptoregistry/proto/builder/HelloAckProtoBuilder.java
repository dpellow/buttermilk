/*
 *  This file is part of Buttermilk
 *  Copyright 2011-2014 David R. Smith All Rights Reserved.
 *
 */
package com.cryptoregistry.proto.builder;

import com.cryptoregistry.c2.key.Curve25519KeyForPublication;
import com.cryptoregistry.protos.Buttermilk.C2KeyContentsProto;
import com.cryptoregistry.protos.Buttermilk.HelloAckProto;
import com.cryptoregistry.protos.Buttermilk.HelloProto;
import com.cryptoregistry.protos.Buttermilk.KeyMetadataProto;
import com.google.protobuf.ByteString;

/**
 * Make a proto buffer out of an C25519KeyForPublication instance. Sent from server
 * 
 * @author Dave
 *
 */
public class HelloAckProtoBuilder {

	final Curve25519KeyForPublication keyContents;
	
	public HelloAckProtoBuilder(Curve25519KeyForPublication keyContents) {
		this.keyContents = keyContents;
	}
	
	public HelloAckProto build() {
		
		KeyMetadataProtoBuilder metaBuilder = new KeyMetadataProtoBuilder(keyContents.metadata);
		KeyMetadataProto metaProto = metaBuilder.build();
		
		C2KeyContentsProto c2Proto = C2KeyContentsProto.newBuilder()
				.setMeta(metaProto)
				.setPublicKey(ByteString.copyFrom(keyContents.publicKey.getBytes()))
				.build();
		
		HelloAckProto hProto = HelloAckProto.newBuilder().setC2KeyContents(c2Proto).build();
		
		return hProto;
		
	}
}
