/*
 *  This file is part of Buttermilk
 *  Copyright 2011-2014 David R. Smith All Rights Reserved.
 *
 */
package com.cryptoregistry.proto.reader;

import java.math.BigInteger;

import com.cryptoregistry.protos.Buttermilk.RSAKeyForPublicationProto;
import com.cryptoregistry.rsa.RSAKeyForPublication;
import com.cryptoregistry.rsa.RSAKeyMetadata;

public class RSAKeyForPublicationProtoReader {

	final RSAKeyForPublicationProto proto;

	public RSAKeyForPublicationProtoReader(RSAKeyForPublicationProto proto) {
		super();
		this.proto = proto;
	}

	public RSAKeyForPublication read() {
		
		// required
		RSAKeyMetadata meta = (RSAKeyMetadata) new KeyMetadataProtoReader(
				proto.getMeta()).read();
		BigInteger modulus = new BigInteger(proto.getModulus().toByteArray());
		BigInteger publicExponent = new BigInteger(proto.getPublicExponent()
				.toByteArray());
		
			// do for publication only
			return new RSAKeyForPublication(meta,modulus,publicExponent);

	}

}
