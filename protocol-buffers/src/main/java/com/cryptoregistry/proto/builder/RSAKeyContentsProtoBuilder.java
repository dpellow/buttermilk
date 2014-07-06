package com.cryptoregistry.proto.builder;

import com.cryptoregistry.protos.Buttermilk.KeyMetadataProto;
import com.cryptoregistry.protos.Buttermilk.RSAKeyContentsProto;
import com.cryptoregistry.rsa.RSAKeyContents;
import com.google.protobuf.ByteString;

/**
 * Make a proto buffer out of an RSAKeyContents instance
 * 
 * @author Dave
 *
 */
public class RSAKeyContentsProtoBuilder {

	final RSAKeyContents keyContents;
	
	public RSAKeyContentsProtoBuilder(RSAKeyContents keyContents) {
		this.keyContents = keyContents;
	}
	
	public RSAKeyContentsProto build() {
		
		KeyMetadataProtoBuilder metaBuilder = new KeyMetadataProtoBuilder(keyContents.metadata);
		KeyMetadataProto metaProto = metaBuilder.build();
		
		RSAKeyContentsProto rsaProto = RSAKeyContentsProto.newBuilder()
				.setMeta(metaProto)
				.setModulus(ByteString.copyFrom(keyContents.modulus.toByteArray()))
				.setPublicExponent(ByteString.copyFrom(keyContents.publicExponent.toByteArray()))
				.setPrivateExponent(ByteString.copyFrom(keyContents.privateExponent.toByteArray()))
				.setP(ByteString.copyFrom(keyContents.p.toByteArray()))
				.setQ(ByteString.copyFrom(keyContents.q.toByteArray()))
				.setDP(ByteString.copyFrom(keyContents.dP.toByteArray()))
				.setDQ(ByteString.copyFrom(keyContents.dQ.toByteArray()))
				.setQInv(ByteString.copyFrom(keyContents.qInv.toByteArray()))
				.build();
		
		return rsaProto;
				
	}

}
