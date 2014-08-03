/*
 *  This file is part of Buttermilk
 *  Copyright 2011-2014 David R. Smith All Rights Reserved.
 *
 */
package com.cryptoregistry.proto.reader;

import java.util.Date;

import com.cryptoregistry.SignatureAlgorithm;
import com.cryptoregistry.proto.compat.SignatureAlgAdapter;
import com.cryptoregistry.protos.Buttermilk.SignatureMetadataProto;
import com.cryptoregistry.protos.Buttermilk.SignatureMetadataProto.SigAlgProto;
import com.cryptoregistry.signature.SignatureMetadata;

public class SignatureMetadataProtoReader {

	final SignatureMetadataProto proto;
	
	public SignatureMetadataProtoReader(SignatureMetadataProto proto) {
		this.proto = proto;
	}
	
	public SignatureMetadata read() {
		String digestAlg = proto.getDigestAlgorithm();
		String handle = proto.getHandle();
		SigAlgProto sap = proto.getSignatureAlgorithm();
		SignatureAlgorithm alg = SignatureAlgAdapter.getSigAlgFor(sap);
		String signedBy = proto.getSignedBy();
		String signedWith = proto.getSignedWith();
		long createdOn = proto.getCreatedOn();
		
		return new SignatureMetadata(handle,new Date(createdOn),alg,digestAlg,signedBy,signedWith);
	}

}
