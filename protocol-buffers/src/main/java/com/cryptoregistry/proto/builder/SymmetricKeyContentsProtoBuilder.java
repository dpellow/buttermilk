/*
 *  This file is part of Buttermilk
 *  Copyright 2011-2014 David R. Smith All Rights Reserved.
 *
 */
package com.cryptoregistry.proto.builder;

import com.cryptoregistry.protos.Buttermilk.KeyMetadataProto;
import com.cryptoregistry.protos.Buttermilk.SymmetricKeyContentsProto;
import com.cryptoregistry.symmetric.SymmetricKeyContents;
import com.google.protobuf.ByteString;

/**
 * Make a proto from a Segment
 * 
 * @author Dave
 *
 */
public class SymmetricKeyContentsProtoBuilder {

	final SymmetricKeyContents keyContents;
	
	public SymmetricKeyContentsProtoBuilder(SymmetricKeyContents key) {
		this.keyContents = key;
	}
	
	public SymmetricKeyContentsProto build() {
		
		KeyMetadataProtoBuilder metaBuilder = new KeyMetadataProtoBuilder(keyContents.metadata);
		KeyMetadataProto metaProto = metaBuilder.build();
		
		SymmetricKeyContentsProto.Builder builder = SymmetricKeyContentsProto.newBuilder();
		builder.setMeta(metaProto)
		.setKey(ByteString.copyFrom(keyContents.getBytes()));
		
		return builder.build();
	}
}
