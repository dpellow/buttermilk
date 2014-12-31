/*
 *  This file is part of Buttermilk
 *  Copyright 2011-2014 David R. Smith All Rights Reserved.
 *
 */
package com.cryptoregistry.proto.builder;

import com.cryptoregistry.ec.ECCustomParameters;
import com.cryptoregistry.ec.ECKeyContents;
import com.cryptoregistry.formats.FormatUtil;
import com.cryptoregistry.ntru.NTRUKeyContents;
import com.cryptoregistry.protos.Buttermilk.CurveDefinitionProto;
import com.cryptoregistry.protos.Buttermilk.ECKeyContentsProto;
import com.cryptoregistry.protos.Buttermilk.KeyMetadataProto;
import com.cryptoregistry.protos.Buttermilk.NTRUKeyContentsProto;
import com.google.protobuf.ByteString;

/**
 * Make a proto buffer out of an NTRUKeyContents instance. 
 * 
 * @author Dave
 *
 */
public class NTRUKeyContentsProtoBuilder {

	final NTRUKeyContents keyContents;
	
	public NTRUKeyContentsProtoBuilder(NTRUKeyContents keyContents) {
		this.keyContents = keyContents;
	}
	
	/**
	 * Note: the encoding hint should be reasonable for encoding Q, such as base16
	 * 
	 * @return
	 */
	public NTRUKeyContentsProto build() {
		
		KeyMetadataProtoBuilder metaBuilder = new KeyMetadataProtoBuilder(keyContents.metadata);
		KeyMetadataProto metaProto = metaBuilder.build();
		NTRUKeyContentsProto.Builder ecProtoBuilder = NTRUKeyContentsProto.newBuilder();
		
		ecProtoBuilder.setMeta(metaProto);
		
		ecProtoBuilder.setParamName(keyContents.parameterEnum.toString());
		
	//	ecProtoBuilder.setT()
		
		return ecProtoBuilder.build();
	}
}
