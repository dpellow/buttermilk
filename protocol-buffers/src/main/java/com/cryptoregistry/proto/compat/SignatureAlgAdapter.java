/*
 *  This file is part of Buttermilk
 *  Copyright 2011-2014 David R. Smith All Rights Reserved.
 *
 */
package com.cryptoregistry.proto.compat;

import com.cryptoregistry.SignatureAlgorithm;
import com.cryptoregistry.protos.Buttermilk.SignatureMetadataProto.SigAlgProto;

public class SignatureAlgAdapter {

	public static SignatureAlgorithm getSigAlgFor(SigAlgProto proto){
		switch(proto){
			case RSA: return SignatureAlgorithm.RSA;
			case ECDSA: return SignatureAlgorithm.ECDSA;
			case ECKCDSA: return SignatureAlgorithm.ECKCDSA;
			default: throw new RuntimeException("Undefined: "+proto);
		}
	}
	
	public static SigAlgProto getProtoFor(SignatureAlgorithm alg){
		switch(alg){
			case RSA: return SigAlgProto.RSA;
			case ECDSA: return SigAlgProto.ECDSA;
			case ECKCDSA: return SigAlgProto.ECKCDSA;
			default: throw new RuntimeException("Undefined: "+alg);
		}
	}

}
