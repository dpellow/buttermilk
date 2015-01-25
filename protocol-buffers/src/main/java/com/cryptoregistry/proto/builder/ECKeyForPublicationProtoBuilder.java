/*
 *  This file is part of Buttermilk
 *  Copyright 2011-2014 David R. Smith All Rights Reserved.
 *
 */
package com.cryptoregistry.proto.builder;

import com.cryptoregistry.ec.ECCustomParameters;
import com.cryptoregistry.ec.ECKeyForPublication;
import com.cryptoregistry.formats.FormatUtil;
import com.cryptoregistry.proto.compat.EncodingAdapter;
import com.cryptoregistry.protos.Buttermilk.CurveDefinitionProto;
import com.cryptoregistry.protos.Buttermilk.ECKeyForPublicationProto;
import com.cryptoregistry.protos.Buttermilk.KeyMetadataProto;
import com.cryptoregistry.protos.Buttermilk.KeyMetadataProto.EncodingHintProto;

/**
 * Make a proto buffer out of an ECKeyForPublication instance. 
 * 
 * @author Dave
 *
 */
public class ECKeyForPublicationProtoBuilder {

	final ECKeyForPublication keyContents;
	
	public ECKeyForPublicationProtoBuilder(ECKeyForPublication keyContents) {
		this.keyContents = keyContents;
	}
	
	/**
	 * Assume Q is encoded in Hex (Base16)
	 * 
	 * @return
	 */
	public ECKeyForPublicationProto build() {
		
		ECKeyForPublicationProto.Builder ecProtoBuilder = ECKeyForPublicationProto.newBuilder();
		
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
			ECKeyForPublicationProto proto = ecProtoBuilder
					.setCurveName(keyContents.curveName)
					.setMeta(km)
					.setQ(FormatUtil.serializeECPoint(keyContents.Q, keyContents.metadata.format.encodingHint))
					.build();
			
			return proto;
	}
}
