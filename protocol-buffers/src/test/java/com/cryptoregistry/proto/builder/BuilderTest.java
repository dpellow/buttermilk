package com.cryptoregistry.proto.builder;

import org.junit.Test;

import com.cryptoregistry.ec.CryptoFactory;
import com.cryptoregistry.ec.ECKeyContents;
import com.cryptoregistry.proto.reader.ECKeyContentsProtoReader;
import com.cryptoregistry.protos.Buttermilk.ECKeyContentsProto;
import com.google.protobuf.InvalidProtocolBufferException;

public class BuilderTest {

	@Test
	public void test0() throws InvalidProtocolBufferException {
		
		// generate a new key and create a Google protocol Buffer object from it:
		final String curveName = "P-256"; 
		ECKeyContents contents = CryptoFactory.INSTANCE.generateKeys(curveName);
		ECKeyContentsProtoBuilder builder = new ECKeyContentsProtoBuilder(contents);
		ECKeyContentsProto keyProto = builder.build();
		
		// convert the key into a compact binary representation:
		byte [] keyBytes = keyProto.toByteArray();
		
		// load the encoding as a java object instance using a reader
		ECKeyContentsProto keyProtoIn = ECKeyContentsProto.parseFrom(keyBytes);
		ECKeyContentsProtoReader reader = new ECKeyContentsProtoReader(keyProtoIn);
		ECKeyContents key = (ECKeyContents) reader.read();
	}

}
