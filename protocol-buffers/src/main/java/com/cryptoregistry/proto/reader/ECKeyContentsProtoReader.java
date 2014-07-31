/*
 *  This file is part of Buttermilk
 *  Copyright 2011-2014 David R. Smith All Rights Reserved.
 *
 */
package com.cryptoregistry.proto.reader;

import com.cryptoregistry.ec.ECKeyForPublication;
import com.cryptoregistry.ec.ECKeyMetadata;
import com.cryptoregistry.protos.Buttermilk.ECKeyContentsProto;


public class ECKeyContentsProtoReader {

	final ECKeyContentsProto proto;

	public ECKeyContentsProtoReader(ECKeyContentsProto proto) {
		super();
		this.proto = proto;
	}

	public ECKeyForPublication read() {
		ECKeyMetadata meta = (ECKeyMetadata) new KeyMetadataReader(
				proto.getMeta()).read();
		
	
		return null;
	}

}
