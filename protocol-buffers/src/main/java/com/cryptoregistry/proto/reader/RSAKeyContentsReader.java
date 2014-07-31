/*
 *  This file is part of Buttermilk
 *  Copyright 2011-2014 David R. Smith All Rights Reserved.
 *
 */
package com.cryptoregistry.proto.reader;

import java.math.BigInteger;

import com.cryptoregistry.protos.Buttermilk.RSAKeyContentsProto;
import com.cryptoregistry.rsa.RSAKeyContents;
import com.cryptoregistry.rsa.RSAKeyForPublication;
import com.cryptoregistry.rsa.RSAKeyMetadata;

public class RSAKeyContentsReader {

	final RSAKeyContentsProto proto;

	public RSAKeyContentsReader(RSAKeyContentsProto proto) {
		super();
		this.proto = proto;
	}

	public RSAKeyForPublication read() {
		
		// required
		RSAKeyMetadata meta = (RSAKeyMetadata) new KeyMetadataReader(
				proto.getMeta()).read();
		BigInteger modulus = new BigInteger(proto.getModulus().toByteArray());
		BigInteger publicExponent = new BigInteger(proto.getPublicExponent()
				.toByteArray());
		
		// optional - full contents
		if(proto.hasPrivateExponent()) {
		
			BigInteger privateExponent = new BigInteger(proto.getPrivateExponent()
				.toByteArray());
			BigInteger P = new BigInteger(proto.getP().toByteArray());
			BigInteger Q = new BigInteger(proto.getQ().toByteArray());
			BigInteger dP = new BigInteger(proto.getDP().toByteArray());
			BigInteger dQ = new BigInteger(proto.getDQ().toByteArray());
			BigInteger qInv = new BigInteger(proto.getQInv().toByteArray());
			RSAKeyContents contents = new RSAKeyContents(meta, modulus,
				publicExponent, privateExponent, P, Q, dP, dQ, qInv);
			return contents;
		
		}else{
			// do for publication only
			RSAKeyForPublication contents = new RSAKeyForPublication(meta,modulus,publicExponent);
			return contents;
		}

	}

}
