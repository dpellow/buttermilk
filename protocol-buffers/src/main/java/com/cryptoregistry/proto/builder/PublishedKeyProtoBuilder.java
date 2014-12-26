/*
 *  This file is part of Buttermilk
 *  Copyright 2011-2014 David R. Smith All Rights Reserved.
 *
 */
package com.cryptoregistry.proto.builder;

import com.cryptoregistry.CryptoKey;
import com.cryptoregistry.KeyGenerationAlgorithm;
import com.cryptoregistry.c2.key.Curve25519KeyForPublication;
import com.cryptoregistry.ec.ECCustomParameters;
import com.cryptoregistry.ec.ECKeyForPublication;
import com.cryptoregistry.formats.EncodingHint;
import com.cryptoregistry.formats.FormatUtil;
import com.cryptoregistry.protos.Buttermilk.C2KeyForPublicationProto;
import com.cryptoregistry.protos.Buttermilk.CurveDefinitionProto;
import com.cryptoregistry.protos.Buttermilk.ECKeyForPublicationProto;
import com.cryptoregistry.protos.Buttermilk.KeyMetadataProto;
import com.cryptoregistry.protos.Buttermilk.PublishedKeyProto;
import com.cryptoregistry.protos.Buttermilk.RSAKeyForPublicationProto;
import com.cryptoregistry.rsa.RSAKeyForPublication;
import com.google.protobuf.ByteString;

/**
 * Make a proto buffer out of any of our defined key types (TODO NTRU, DSA)
 * 
 * @author Dave
 *
 */
public class PublishedKeyProtoBuilder {

	final CryptoKey keyContents;
	
	public PublishedKeyProtoBuilder(CryptoKey keyContents) {
		this.keyContents = keyContents;
	}
	
	public PublishedKeyProto build() {
		
		KeyMetadataProtoBuilder metaBuilder = new KeyMetadataProtoBuilder(keyContents.getMetadata());
		KeyMetadataProto metaProto = metaBuilder.build();
		
		KeyGenerationAlgorithm alg = keyContents.getMetadata().getKeyAlgorithm();
		
		switch(alg) {
			case Symmetric: {
			
				break;
			}
			case Curve25519 : {
				Curve25519KeyForPublication key = (Curve25519KeyForPublication)keyContents;
				C2KeyForPublicationProto c2Proto = C2KeyForPublicationProto.newBuilder()
						.setMeta(metaProto)
						.setPublicKey(ByteString.copyFrom(key.publicKey.getBytes()))
						.build();
				PublishedKeyProto pkp = PublishedKeyProto.newBuilder().setC2(c2Proto).build();
				return pkp;
			}
			case EC : {
				ECKeyForPublication key = (ECKeyForPublication)keyContents;
				ECKeyForPublicationProto.Builder ecProtoBuilder = ECKeyForPublicationProto.newBuilder();
				ecProtoBuilder.setMeta(metaProto);
				
				if(key.usesNamedCurve()) {
					ecProtoBuilder.setCurveName(key.curveName);
				}else{
					ECCustomParameters params = (ECCustomParameters) key.customCurveDefinition;
					CurveDefinitionProtoBuilder custBuilder = new CurveDefinitionProtoBuilder(params);
					CurveDefinitionProto curveDefProto = custBuilder.build();
					ecProtoBuilder.setCurveDefinition(curveDefProto);
				}
				
				ecProtoBuilder.setQ(FormatUtil.serializeECPoint(key.Q, EncodingHint.Base16));
				ECKeyForPublicationProto ekp = ecProtoBuilder.build();
				PublishedKeyProto pkp = PublishedKeyProto.newBuilder().setEc(ekp).build();
				return pkp;
			}
			case RSA : {
				RSAKeyForPublication key = (RSAKeyForPublication) keyContents;
				RSAKeyForPublicationProto rsaProto = RSAKeyForPublicationProto.newBuilder()
						.setMeta(metaProto)
						.setModulus(ByteString.copyFrom(key.modulus.toByteArray()))
						.setPublicExponent(ByteString.copyFrom(key.publicExponent.toByteArray()))
						.build();
				
				PublishedKeyProto pkp = PublishedKeyProto.newBuilder().setRsa(rsaProto).build();
				return pkp;
			}
			case DSA: {
				// TODO DSA support here
				break;
			}
			case NTRU: {
				// TODO NTRU support here
				break;
			}
			default: throw new RuntimeException("Unknown KeyGenrationAlgorithm");
		}
		
		return null;
		
	}
}
