/*
 *  This file is part of Buttermilk
 *  Copyright 2011-2014 David R. Smith All Rights Reserved.
 *
 */
package com.cryptoregistry.proto.reader;

import com.cryptoregistry.c2.key.AgreementPrivateKey;
import com.cryptoregistry.c2.key.C2KeyMetadata;
import com.cryptoregistry.c2.key.Curve25519KeyContents;
import com.cryptoregistry.c2.key.Curve25519KeyForPublication;
import com.cryptoregistry.c2.key.PublicKey;
import com.cryptoregistry.c2.key.SigningPrivateKey;
import com.cryptoregistry.protos.Buttermilk.C2KeyContentsProto;


public class C2KeyContentsProtoReader {

	final C2KeyContentsProto proto;

	public C2KeyContentsProtoReader(C2KeyContentsProto proto) {
		super();
		this.proto = proto;
	}

	public Curve25519KeyForPublication read() {
		C2KeyMetadata meta = (C2KeyMetadata) new KeyMetadataProtoReader(
				proto.getMeta()).read();
		
		PublicKey pk = new PublicKey(proto.getPublicKey().toByteArray());
		
		if(proto.hasAgreementPrivateKey()) {
			SigningPrivateKey spk = new SigningPrivateKey(proto.getSigningPrivateKey().toByteArray());
			AgreementPrivateKey apk = new AgreementPrivateKey(proto.getAgreementPrivateKey().toByteArray());
			Curve25519KeyContents c = new Curve25519KeyContents(meta,pk,spk,apk);
			return c;
		}else{
			Curve25519KeyForPublication fp = new Curve25519KeyForPublication(meta,pk);
			return fp;
		}
	}

}
