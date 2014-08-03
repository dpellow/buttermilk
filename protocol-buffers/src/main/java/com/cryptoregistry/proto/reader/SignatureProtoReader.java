/*
 *  This file is part of Buttermilk
 *  Copyright 2011-2014 David R. Smith All Rights Reserved.
 *
 */
package com.cryptoregistry.proto.reader;

import java.math.BigInteger;
import java.util.List;

import com.cryptoregistry.protos.Buttermilk.ListProto;
import com.cryptoregistry.protos.Buttermilk.SignatureProto;
import com.cryptoregistry.signature.C2CryptoSignature;
import com.cryptoregistry.signature.C2Signature;
import com.cryptoregistry.signature.CryptoSignature;
import com.cryptoregistry.signature.ECDSACryptoSignature;
import com.cryptoregistry.signature.ECDSASignature;
import com.cryptoregistry.signature.RSACryptoSignature;
import com.cryptoregistry.signature.RSASignature;
import com.cryptoregistry.signature.SignatureMetadata;
import com.cryptoregistry.util.ArmoredString;
import com.google.protobuf.ByteString;

public class SignatureProtoReader {

	final SignatureProto proto;
	
	public SignatureProtoReader(SignatureProto proto) {
		this.proto = proto;
	}
	
	public CryptoSignature read() {
		ByteString b0 = proto.getB0();
		ByteString b1 = null;
		if(proto.hasB1()) {
			b1 = proto.getB1();
		}
		SignatureMetadataProtoReader mpr = new SignatureMetadataProtoReader(proto.getMeta());
		SignatureMetadata meta = mpr.read();
		ListProto listProto = proto.getDataRefs();
		ListProtoReader lr = new ListProtoReader(listProto);
		List<String> list = lr.read();
		switch(meta.sigAlg) {
			case RSA: {
				ArmoredString s = new ArmoredString(b0.toByteArray());
				RSASignature sig = new RSASignature(s);
				return new RSACryptoSignature(meta,list,sig);
			}
			case ECDSA : {
				BigInteger r = new BigInteger(b0.toByteArray());
				BigInteger s = new BigInteger(b1.toByteArray());
				ECDSASignature sig = new ECDSASignature(r,s);
				return new ECDSACryptoSignature(meta,list,sig);
			}
			case ECKCDSA : {
				ArmoredString r = new ArmoredString(b0.toByteArray());
				ArmoredString v = new ArmoredString(b1.toByteArray());
				C2Signature sig = new C2Signature(r,v);
				return new C2CryptoSignature(meta,list,sig);
			}
			default: throw new RuntimeException("Unknown sig alg:"+meta.sigAlg);
		}
	}

}
