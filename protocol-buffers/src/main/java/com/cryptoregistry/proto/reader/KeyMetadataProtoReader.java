/*
 *  This file is part of Buttermilk
 *  Copyright 2011-2014 David R. Smith All Rights Reserved.
 *
 */
package com.cryptoregistry.proto.reader;

import java.util.Date;

import com.cryptoregistry.CryptoKeyMetadata;
import com.cryptoregistry.KeyGenerationAlgorithm;
import com.cryptoregistry.c2.key.C2KeyMetadata;
import com.cryptoregistry.ec.ECKeyMetadata;
import com.cryptoregistry.formats.EncodingHint;
import com.cryptoregistry.formats.KeyFormat;
import com.cryptoregistry.formats.Mode;
import com.cryptoregistry.ntru.NTRUKeyMetadata;
import com.cryptoregistry.proto.compat.EncodingAdapter;
import com.cryptoregistry.protos.Buttermilk.KeyMetadataProto;
import com.cryptoregistry.rsa.RSAKeyMetadata;

public class KeyMetadataProtoReader {

	KeyMetadataProto proto;

	public KeyMetadataProtoReader(KeyMetadataProto proto) {
		super();
		this.proto = proto;
	}
	
	public CryptoKeyMetadata read() {
		String uuid = proto.getHandle();
		String alg = proto.getKeyGenerationAlgorithm();
		Date createdOn = new Date(proto.getCreatedOn());
		EncodingHint encodingHint = EncodingAdapter.getEncodingFor(proto.getEncodingHint());
		KeyFormat format = new KeyFormat(encodingHint,Mode.UNSECURED);
		KeyGenerationAlgorithm kga = KeyGenerationAlgorithm.valueOf(alg);
	
		switch(kga){
			case RSA: {
				RSAKeyMetadata meta = new RSAKeyMetadata(uuid,createdOn,format);
				return meta;
			}
			case Curve25519: {
				C2KeyMetadata meta = new C2KeyMetadata(uuid,createdOn,format);
				return meta;
			}
			case EC:{
				ECKeyMetadata meta = new ECKeyMetadata(uuid,createdOn,format);
				return meta;
			}
			case NTRU:{
				NTRUKeyMetadata meta = new NTRUKeyMetadata(uuid,createdOn,format);
				return meta;
			}
			default: throw new RuntimeException("No such alg implemented: "+kga);
		}
	} 

}
