/*
 *  This file is part of Buttermilk
 *  Copyright 2011-2014 David R. Smith All Rights Reserved.
 *
 */
package com.cryptoregistry.proto.builder;


import com.cryptoregistry.protos.Buttermilk.ListProto;
import com.cryptoregistry.protos.Buttermilk.SignatureMetadataProto;
import com.cryptoregistry.protos.Buttermilk.SignatureProto;
import com.cryptoregistry.signature.CryptoSignature;
import com.google.protobuf.ByteString;

public class SignatureProtoBuilder {

	final CryptoSignature sig;
	
	public SignatureProtoBuilder(CryptoSignature sig) {
		this.sig = sig;
	}
	
	public SignatureProto build() {
		ListProtoBuilder l_builder = new ListProtoBuilder(sig.dataRefs);
		ListProto l_proto = l_builder.build();
		ByteString b1 = ByteString.copyFrom(sig.signatureBytes().b1());
		ByteString b2 = null;
		if(sig.signatureBytes().b2() != null){
			b2 = ByteString.copyFrom(sig.signatureBytes().b2());
		}
		SignatureMetadataProtoBuilder m_builder = new SignatureMetadataProtoBuilder(sig.metadata);
		SignatureMetadataProto metaProto = m_builder.build();
		SignatureProto.Builder sbuilder = SignatureProto.newBuilder()
				.setMeta(metaProto)
				.setB0(b1)
				.setDataRefs(l_proto);
		if(b2 != null) sbuilder.setB1(b2);
		return sbuilder.build();
	
	}

}
