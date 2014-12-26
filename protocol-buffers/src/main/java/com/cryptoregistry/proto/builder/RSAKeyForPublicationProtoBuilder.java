/*
 *  This file is part of Buttermilk
 *  Copyright 2011-2014 David R. Smith All Rights Reserved.
 *
 */
package com.cryptoregistry.proto.builder;

import com.cryptoregistry.protos.Buttermilk.KeyMetadataProto;
import com.cryptoregistry.protos.Buttermilk.RSAKeyForPublicationProto;
import com.cryptoregistry.rsa.RSAKeyForPublication;
import com.google.protobuf.ByteString;

/**<p>
 * Make a proto buffer out of an RSAKeyForPublication instance. If a full RSAKeyContents object is
 * passed in, we still get a proto suitable for publication as RSAKeyContentsProto has optional
 * fields for the private key portions
 * </p>
 * 
 * @author Dave
 *
 */
public class RSAKeyForPublicationProtoBuilder {

	final RSAKeyForPublication keyContents;
	
	public RSAKeyForPublicationProtoBuilder(RSAKeyForPublication keyContents) {
		this.keyContents = keyContents;
	}
	
	public RSAKeyForPublicationProto build() {
		
		KeyMetadataProtoBuilder metaBuilder = new KeyMetadataProtoBuilder(keyContents.metadata);
		KeyMetadataProto metaProto = metaBuilder.build();
		
		RSAKeyForPublicationProto rsaProto = RSAKeyForPublicationProto.newBuilder()
				.setMeta(metaProto)
				.setModulus(ByteString.copyFrom(keyContents.modulus.toByteArray()))
				.setPublicExponent(ByteString.copyFrom(keyContents.publicExponent.toByteArray()))
				.build();
		
		return rsaProto;
		
	}
}
