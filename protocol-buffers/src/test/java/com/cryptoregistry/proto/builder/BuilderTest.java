package com.cryptoregistry.proto.builder;

import junit.framework.Assert;

import org.junit.Test;

import com.cryptoregistry.c2.key.Curve25519KeyContents;
import com.cryptoregistry.ec.CryptoFactory;
import com.cryptoregistry.ec.ECKeyContents;
import com.cryptoregistry.ec.ECKeyForPublication;
import com.cryptoregistry.proto.reader.C2KeyContentsProtoReader;
import com.cryptoregistry.proto.reader.ECKeyContentsProtoReader;
import com.cryptoregistry.proto.reader.ECKeyForPublicationProtoReader;
import com.cryptoregistry.protos.Buttermilk.C2KeyContentsProto;
import com.cryptoregistry.protos.Buttermilk.ECKeyContentsProto;
import com.cryptoregistry.protos.Buttermilk.ECKeyForPublicationProto;
import com.google.protobuf.InvalidProtocolBufferException;

public class BuilderTest {

	@Test
	public void test0() throws InvalidProtocolBufferException {
		
		// generate a new key and create a Google Protocol Buffer object from it:
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
		
		boolean equal = contents.equals(key);
		Assert.assertTrue(equal);
		
	}
	
	@Test
	public void test2() throws InvalidProtocolBufferException {
		
		// generate a new key and create a Google Protocol Buffer object from it using a builder:
		final String curveName = "P-256"; 
		ECKeyContents contents = CryptoFactory.INSTANCE.generateKeys(curveName);
		ECKeyForPublication pub = contents.cloneForPublication();
		ECKeyForPublicationProtoBuilder builder = new ECKeyForPublicationProtoBuilder(pub);
		ECKeyForPublicationProto keyProto = builder.build();
		
		// convert the key into a compact binary representation:
		byte [] keyBytes = keyProto.toByteArray();
		
		// load the encoding as a new instance using a reader
		ECKeyForPublicationProto keyProtoIn = ECKeyForPublicationProto.parseFrom(keyBytes);
		ECKeyForPublicationProtoReader reader = new ECKeyForPublicationProtoReader(keyProtoIn);
		ECKeyForPublication key = reader.read();
		
		boolean equal = pub.equals(key);
		Assert.assertTrue(equal);
		
	}
	
	@Test
	public void test1() throws InvalidProtocolBufferException {
		
		// generate a new key and create a Google Protocol Buffer object from it:
		Curve25519KeyContents contents = com.cryptoregistry.c2.CryptoFactory.INSTANCE.generateKeys();
		C2KeyContentsProtoBuilder builder = new C2KeyContentsProtoBuilder(contents);
		C2KeyContentsProto keyProto = builder.build();
		
		// convert the key into a compact binary representation:
		byte [] keyBytes = keyProto.toByteArray();
		
		// load the encoding as a java object instance using a reader
		C2KeyContentsProto keyProtoIn = C2KeyContentsProto.parseFrom(keyBytes);
		C2KeyContentsProtoReader reader = new C2KeyContentsProtoReader(keyProtoIn);
		Curve25519KeyContents key = (Curve25519KeyContents) reader.read();
		
		boolean equal = contents.equals(key);
		Assert.assertTrue(equal);
		
	}

}
