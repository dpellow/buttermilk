/*
 *  This file is part of Buttermilk
 *  Copyright 2011-2015 David R. Smith All Rights Reserved.
 *
 */
package com.cryptoregistry.proto.reader;

import junit.framework.Assert;

import org.junit.Test;

import com.cryptoregistry.proto.compat.EncodingAdapter;
import com.cryptoregistry.protos.Buttermilk;
import com.cryptoregistry.protos.Buttermilk.KeyMetadataProto;
import com.cryptoregistry.rsa.CryptoFactory;
import com.cryptoregistry.rsa.RSAKeyContents;
import com.google.protobuf.ByteString;

public class TestReaders {

	@Test
	public void test1() {
		
		RSAKeyContents contents = CryptoFactory.INSTANCE.generateKeys();
		
		KeyMetadataProto km = Buttermilk.KeyMetadataProto.newBuilder()
		.setEncodingHint(EncodingAdapter.getProtoFor(contents.metadata.format.encodingHint))
		.setHandle(contents.getMetadata().getHandle())
		.setKeyGenerationAlgorithm(contents.metadata.getKeyAlgorithm().toString())
		.setCreatedOn(contents.metadata.getCreatedOn().getTime())
		.setStrength(contents.metadata.strength)
		.setCertainty(contents.metadata.certainty)
		.build();
		
		Buttermilk.RSAKeyContentsProto rsaProto = Buttermilk.RSAKeyContentsProto.newBuilder()
		.setMeta(km)
		.setModulus(ByteString.copyFrom(contents.modulus.toByteArray()))
		.setPublicExponent(ByteString.copyFrom(contents.publicExponent.toByteArray()))
		.setPrivateExponent(ByteString.copyFrom(contents.privateExponent.toByteArray()))
		.setDP(ByteString.copyFrom(contents.dP.toByteArray()))
		.setDQ(ByteString.copyFrom(contents.dQ.toByteArray()))
		.setP(ByteString.copyFrom(contents.p.toByteArray()))
		.setQ(ByteString.copyFrom(contents.q.toByteArray()))
		.setQInv(ByteString.copyFrom(contents.qInv.toByteArray())).build();
		
		RSAKeyContentsProtoReader reader = new RSAKeyContentsProtoReader(rsaProto);
		RSAKeyContents _contents = (RSAKeyContents) reader.read();
		
		Assert.assertEquals(contents,_contents);
	}

}
