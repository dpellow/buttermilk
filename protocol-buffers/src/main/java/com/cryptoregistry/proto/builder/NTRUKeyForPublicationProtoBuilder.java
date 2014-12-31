/*
 *  This file is part of Buttermilk
 *  Copyright 2011-2014 David R. Smith All Rights Reserved.
 *
 */
package com.cryptoregistry.proto.builder;

import com.cryptoregistry.ntru.NTRUKeyForPublication;
import com.cryptoregistry.protos.Buttermilk.KeyMetadataProto;
import com.cryptoregistry.protos.Buttermilk.NTRUKeyForPublicationProto;

/**
 * Make a proto buffer out of an NTRUKeyContents instance. 
 * 
 * @author Dave
 *
 */
public class NTRUKeyForPublicationProtoBuilder {

	final NTRUKeyForPublication keyContents;
	
	public NTRUKeyForPublicationProtoBuilder(NTRUKeyForPublication keyContents) {
		this.keyContents = keyContents;
	}
	
	/**
	 * Note: the encoding hint should be reasonable for encoding Q, such as base16
	 * 
	 * @return
	 */
	public NTRUKeyForPublicationProto build() {
		
		KeyMetadataProtoBuilder metaBuilder = new KeyMetadataProtoBuilder(keyContents.metadata);
		KeyMetadataProto metaProto = metaBuilder.build();
		NTRUKeyForPublicationProto.Builder ecProtoBuilder = NTRUKeyForPublicationProto.newBuilder();
		
		ecProtoBuilder.setMeta(metaProto);
		
		ecProtoBuilder.setParamName(keyContents.parameterEnum.toString());
	// TODO
	//	ecProtoBuilder.setT()
		
		return ecProtoBuilder.build();
	}
}
