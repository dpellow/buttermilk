package com.cryptoregistry.protos;

import java.io.StringWriter;
import java.math.BigInteger;
import java.util.Date;

import junit.framework.Assert;

import org.junit.Test;

import com.cryptoregistry.ec.ECKeyContents;
import com.cryptoregistry.ec.ECKeyMetadata;
import com.cryptoregistry.formats.EncodingHint;
import com.cryptoregistry.formats.FormatUtil;
import com.cryptoregistry.formats.JSONFormatter;
import com.cryptoregistry.formats.KeyFormat;
import com.cryptoregistry.formats.Mode;
import com.cryptoregistry.proto.compat.EncodingAdapter;
import com.cryptoregistry.protos.Buttermilk.KeyMetadataProto;
import com.cryptoregistry.protos.Buttermilk.KeyMetadataProto.EncodingHintProto;
import com.cryptoregistry.rsa.CryptoFactory;
import com.cryptoregistry.rsa.RSAKeyContents;
import com.cryptoregistry.rsa.RSAKeyMetadata;
import com.google.protobuf.ByteString;
import com.google.protobuf.InvalidProtocolBufferException;

public class ProtoTest {
	
	@Test
	public void test1() {
		
		ECKeyContents contents = com.cryptoregistry.ec.CryptoFactory.INSTANCE.generateKeys("P-256");
		
		StringWriter s1 = new StringWriter();
		JSONFormatter f = new JSONFormatter("Chinese Knees");
		f.add(contents);
		f.format(s1);
		System.err.println(s1);

		EncodingHintProto hintProtoIn = EncodingAdapter.getProtoFor(contents.metadata.format.encodingHint);
		
		KeyMetadataProto km = Buttermilk.KeyMetadataProto.newBuilder()
				.setEncodingHint(hintProtoIn)
				.setHandle(contents.getMetadata().getHandle())
				.setKeyGenerationAlgorithm(contents.metadata.getKeyAlgorithm().toString())
				.setCreatedOn(contents.metadata.getCreatedOn().getTime())
				.build();
		Buttermilk.ECKeyContentsProto proto = Buttermilk.ECKeyContentsProto.newBuilder()
				.setCurveName(contents.curveName)
				.setMeta(km)
				.setQ(FormatUtil.serializeECPoint(contents.Q, contents.metadata.format.encodingHint))
				.setD(ByteString.copyFrom(contents.d.toByteArray()))
				.build();
		
		byte [] key = proto.toByteArray();
		
		try {
			Buttermilk.ECKeyContentsProto parsed = Buttermilk.ECKeyContentsProto.parseFrom(key);
			String curveName = parsed.getCurveName();
			String Q = parsed.getQ();
			BigInteger D = new BigInteger(parsed.getD().toByteArray());
			Date createdOn = new Date(parsed.getMeta().getCreatedOn());
			String handle = parsed.getMeta().getHandle();
			EncodingHintProto hintProto = parsed.getMeta().getEncodingHint();
			EncodingHint hint = EncodingAdapter.getEncodingHintFor(hintProto);
			KeyFormat format = new KeyFormat(hint,Mode.UNSECURED,null);
			ECKeyContents result = new ECKeyContents(
					new ECKeyMetadata(handle,createdOn,format),
					FormatUtil.parseECPoint(curveName, hint, Q),
					curveName,
					D
			);
			
			s1 = new StringWriter();
			f = new JSONFormatter("Chinese Knees");
			f.add(result);
			f.format(s1);
			System.err.println(s1);
			
			boolean equal = contents.equals(result);
			
			Assert.assertTrue(equal);
			
			
		}catch(Exception x){
			x.printStackTrace();
		}
		
	}

	@Test
	public void test0() {
		
		RSAKeyContents contents = CryptoFactory.INSTANCE.generateKeys();
		KeyFormat format = contents.metadata.format;
		
		StringWriter s1 = new StringWriter();
		JSONFormatter f = new JSONFormatter("Chinese Knees");
		f.add(contents);
		f.format(s1);
		System.err.println(s1);
		
		KeyMetadataProto km = Buttermilk.KeyMetadataProto.newBuilder()
		.setEncodingHint(Buttermilk.KeyMetadataProto.EncodingHintProto.RAWBYTES)
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
		
		byte [] rsaKey = rsaProto.toByteArray();
		
		try {
			Buttermilk.RSAKeyContentsProto parsed = Buttermilk.RSAKeyContentsProto.parseFrom(rsaKey);
			Date createdOn = new Date(parsed.getMeta().getCreatedOn());
			String handle = parsed.getMeta().getHandle();
			int certainty = parsed.getMeta().getCertainty();
			int strength = parsed.getMeta().getStrength();
			RSAKeyContents key = new RSAKeyContents(
					new RSAKeyMetadata(handle,createdOn,format,strength,certainty),
					new BigInteger(parsed.getModulus().toByteArray()),
					new BigInteger(parsed.getPublicExponent().toByteArray()),
					new BigInteger(parsed.getPrivateExponent().toByteArray()),
					new BigInteger(parsed.getP().toByteArray()),
					new BigInteger(parsed.getQ().toByteArray()),
					new BigInteger(parsed.getDP().toByteArray()),
					new BigInteger(parsed.getDQ().toByteArray()),
					new BigInteger(parsed.getQInv().toByteArray())
			);
			
			s1 = new StringWriter();
			f = new JSONFormatter("Chinese Knees");
			f.add(key);
			f.format(s1);
			System.err.println(s1);
			
			Assert.assertEquals(contents, key);
			
		} catch (InvalidProtocolBufferException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
