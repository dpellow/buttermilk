package com.cryptoregistry.proto.builder;

import com.cryptoregistry.protos.Buttermilk.KeyMetadataProto;
import com.cryptoregistry.protos.Buttermilk.RSAKeyContentsProto;
import com.cryptoregistry.rsa.RSAKeyForPublication;
import com.google.protobuf.ByteString;

/**<p>
 * Make a proto buffer out of an RSAKeyForPublication instance. If a full RSAKeyContents object is
 * passed in, we still get a proto suitable for publication as RSAKeyContentsProto has optional
 * fields for the private key portions
 * </p>
 * 
 * <p>
 * In other words, this is a fast way to make a public key proto
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
	
	public RSAKeyContentsProto build() {
		
		KeyMetadataProtoBuilder metaBuilder = new KeyMetadataProtoBuilder(keyContents.metadata);
		KeyMetadataProto metaProto = metaBuilder.build();
		
		RSAKeyContentsProto rsaProto = RSAKeyContentsProto.newBuilder()
				.setMeta(metaProto)
				.setModulus(ByteString.copyFrom(keyContents.modulus.toByteArray()))
				.setPublicExponent(ByteString.copyFrom(keyContents.publicExponent.toByteArray()))
				.build();
		
		return rsaProto;
		
	}
}
