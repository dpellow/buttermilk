/*
 *  This file is part of Buttermilk
 *  Copyright 2011-2014 David R. Smith All Rights Reserved.
 *
 */
package com.cryptoregistry.proto.builder;

import com.cryptoregistry.ec.ECCustomParameters;
import com.cryptoregistry.ec.ECKeyContents;
import com.cryptoregistry.formats.FormatUtil;
import com.cryptoregistry.protos.Buttermilk.CurveDefinitionProto;
import com.cryptoregistry.protos.Buttermilk.ECKeyContentsProto;
import com.cryptoregistry.protos.Buttermilk.KeyMetadataProto;
import com.cryptoregistry.protos.Buttermilk.KeyMetadataProto.EncodingHintProto;
import com.google.protobuf.ByteString;

import com.cryptoregistry.proto.compat.EncodingAdapter;

/**
 * Make a proto buffer out of an ECKeyForPublication instance. 
 * 
 * @author Dave
 *
 */
public class ECKeyContentsProtoBuilder {

	final ECKeyContents keyContents;
	
	public ECKeyContentsProtoBuilder(ECKeyContents keyContents) {
		this.keyContents = keyContents;
	}
	
	/**
	 * Note: the encoding hint should be reasonable for encoding Q, such as base16
	 * 
	 * @return
	 */
	public ECKeyContentsProto build() {
		
		ECKeyContentsProto.Builder ecProtoBuilder = ECKeyContentsProto.newBuilder();
		
		if(keyContents.usesNamedCurve()) {
				ecProtoBuilder.setCurveName(keyContents.curveName);
			}else{
				ECCustomParameters params = (ECCustomParameters) keyContents.customCurveDefinition;
				CurveDefinitionProtoBuilder custBuilder = new CurveDefinitionProtoBuilder(params);
				CurveDefinitionProto curveDefProto = custBuilder.build();
				ecProtoBuilder.setCurveDefinition(curveDefProto);
			}

		EncodingHintProto hintProtoIn = EncodingAdapter.getProtoFor(keyContents.metadata.format.encodingHint);
			
			KeyMetadataProto km = KeyMetadataProto.newBuilder()
					.setEncodingHint(hintProtoIn)
					.setHandle(keyContents.getMetadata().getHandle())
					.setKeyGenerationAlgorithm(keyContents.metadata.getKeyAlgorithm().toString())
					.setCreatedOn(keyContents.metadata.getCreatedOn().getTime())
					.build();
			ECKeyContentsProto proto = ecProtoBuilder
					.setCurveName(keyContents.curveName)
					.setMeta(km)
					.setQ(FormatUtil.serializeECPoint(keyContents.Q, keyContents.metadata.format.encodingHint))
					.setD(ByteString.copyFrom(keyContents.d.toByteArray()))
					.build();
			
			return proto;
	}
}
