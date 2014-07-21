package com.cryptoregistry.protos;

import java.math.BigInteger;
import java.util.Date;

import junit.framework.Assert;

import org.junit.Test;

import com.cryptoregistry.formats.KeyFormat;
import com.cryptoregistry.protos.Buttermilk.KeyMetadataProto;
import com.cryptoregistry.rsa.CryptoFactory;
import com.cryptoregistry.rsa.RSAKeyContents;
import com.cryptoregistry.rsa.RSAKeyMetadata;
import com.google.protobuf.ByteString;
import com.google.protobuf.InvalidProtocolBufferException;

public class ProtoTest {

	@Test
	public void test0() {
		
		RSAKeyContents contents = CryptoFactory.INSTANCE.generateKeys();
		KeyFormat format = contents.metadata.format;
		
		KeyMetadataProto km = Buttermilk.KeyMetadataProto.newBuilder()
		.setEncoding(Buttermilk.KeyMetadataProto.EncodingProto.RAWBYTES) // hard code to rawbytes
		.setHandle(contents.getMetadata().getHandle())
		.setKeyGenerationAlgorithm(contents.metadata.getKeyAlgorithm().toString())
		.setCreatedOn(contents.metadata.getCreatedOn().getTime()).build();
		
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
		
		byte [] rsaKey = rsaProto.toByteArray();
		
		try {
			Buttermilk.RSAKeyContentsProto parsed = Buttermilk.RSAKeyContentsProto.parseFrom(rsaKey);
			Date createdOn = new Date(parsed.getMeta().getCreatedOn());
		//	EncodingProto enc =parsed.getMeta().getEncoding();
			String handle = parsed.getMeta().getHandle();
		//	KeyFormat format = new KeyFormat(Encoding.NoEncoding,Mode.UNSECURED);
			RSAKeyContents key = new RSAKeyContents(
					new RSAKeyMetadata(handle,createdOn,format),
					new BigInteger(parsed.getModulus().toByteArray()),
					new BigInteger(parsed.getPublicExponent().toByteArray()),
					new BigInteger(parsed.getPrivateExponent().toByteArray()),
					new BigInteger(parsed.getP().toByteArray()),
					new BigInteger(parsed.getQ().toByteArray()),
					new BigInteger(parsed.getDP().toByteArray()),
					new BigInteger(parsed.getDQ().toByteArray()),
					new BigInteger(parsed.getQInv().toByteArray())
			);
			
			Assert.assertEquals(contents, key);
			
		} catch (InvalidProtocolBufferException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}

}
