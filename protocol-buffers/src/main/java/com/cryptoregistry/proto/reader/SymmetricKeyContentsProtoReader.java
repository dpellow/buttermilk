/*
 *  This file is part of Buttermilk
 *  Copyright 2011-2014 David R. Smith All Rights Reserved.
 *
 */
package com.cryptoregistry.proto.reader;

import com.cryptoregistry.protos.Buttermilk.SymmetricKeyContentsProto;
import com.cryptoregistry.symmetric.SymmetricKeyContents;
import com.cryptoregistry.symmetric.SymmetricKeyMetadata;

public class SymmetricKeyContentsProtoReader {

	final SymmetricKeyContentsProto proto;

	public SymmetricKeyContentsProtoReader(SymmetricKeyContentsProto proto) {
		super();
		this.proto = proto;
	}

	public SymmetricKeyContents read() {
		 SymmetricKeyMetadata meta = (SymmetricKeyMetadata) new KeyMetadataProtoReader(
					proto.getMeta()).read();
		 return new SymmetricKeyContents(meta,proto.getKey().toByteArray());
	}
}
