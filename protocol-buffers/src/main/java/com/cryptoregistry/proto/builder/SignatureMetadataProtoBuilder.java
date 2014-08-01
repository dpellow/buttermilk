/*
 *  This file is part of Buttermilk
 *  Copyright 2011-2014 David R. Smith All Rights Reserved.
 *
 */
package com.cryptoregistry.proto.builder;

import com.cryptoregistry.SignatureAlgorithm;
import com.cryptoregistry.protos.Buttermilk.SignatureMetadataProto;
import com.cryptoregistry.protos.Buttermilk.SignatureMetadataProto.SigAlgProto;
import com.cryptoregistry.signature.SignatureMetadata;

public class SignatureMetadataProtoBuilder {

	final SignatureMetadata meta;
	
	public SignatureMetadataProtoBuilder(SignatureMetadata meta) {
		this.meta = meta;
	}
	
	public SignatureMetadataProto build() {
		
		SigAlgProto sigAlg = null;
		
		SignatureAlgorithm alg = meta.sigAlg;
		switch(alg){
			case RSA: sigAlg = SigAlgProto.RSA; break; 
			case ECDSA: sigAlg = SigAlgProto.ECDSA; break; 
			case ECKCDSA: sigAlg = SigAlgProto.ECKCDSA  ; break; 
			default: throw new RuntimeException("Unknown: "+alg);
		}
		
		return SignatureMetadataProto.newBuilder()
		.setHandle(meta.getHandle())
		.setCreatedOn(meta.createdOn.getTime())
		.setSignatureAlgorithm(sigAlg)
		.setDigestAlgorithm(meta.digestAlg)
		.setSignedWith(meta.signedWith)
		.setSignedBy(meta.signedBy)
		.build();
	}

}
