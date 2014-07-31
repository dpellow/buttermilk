package com.cryptoregistry.proto.builder;

import com.cryptoregistry.c2.key.Curve25519KeyForPublication;
import com.cryptoregistry.protos.Buttermilk.C2KeyContentsProto;
import com.cryptoregistry.protos.Buttermilk.KeyMetadataProto;
import com.google.protobuf.ByteString;

/**
 * Make a proto buffer out of an C25519KeyForPublication instance. 
 * 
 * @author Dave
 *
 */
public class C2KeyForPublicationProtoBuilder {

	final Curve25519KeyForPublication keyContents;
	
	public C2KeyForPublicationProtoBuilder(Curve25519KeyForPublication keyContents) {
		this.keyContents = keyContents;
	}
	
	public C2KeyContentsProto build() {
		
		KeyMetadataProtoBuilder metaBuilder = new KeyMetadataProtoBuilder(keyContents.metadata);
		KeyMetadataProto metaProto = metaBuilder.build();
		
		C2KeyContentsProto c2Proto = C2KeyContentsProto.newBuilder()
				.setMeta(metaProto)
				.setPublicKey(ByteString.copyFrom(keyContents.publicKey.getBytes()))
				.build();
		
		return c2Proto;
		
	}
}
