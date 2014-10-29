/*
 *  This file is part of Buttermilk
 *  Copyright 2011-2014 David R. Smith All Rights Reserved.
 *
 */
package com.cryptoregistry.proto.builder;

import com.cryptoregistry.c2.key.Curve25519KeyForPublication;
import com.cryptoregistry.protos.Buttermilk.C2KeyContentsProto;
import com.cryptoregistry.protos.Buttermilk.HelloProto;
import com.cryptoregistry.protos.Buttermilk.KeyMetadataProto;
import com.cryptoregistry.protos.Buttermilk.SegmentProto;
import com.google.protobuf.ByteString;

/**
 * Make a proto buffer out of an C25519KeyForPublication instance. 
 * 
 * @author Dave
 *
 */
public class SegmentProtoBuilder {

	final byte [] data;
	final byte [] iv;
	
	public SegmentProtoBuilder(byte[]data,byte[]iv) {
		this.data = data;
		this.iv = iv;
	}
	
	public SegmentProto build() {
		
		SegmentProto.Builder builder = SegmentProto.newBuilder();
		builder.setData(ByteString.copyFrom(data));
		if(iv != null) {
			builder.setIv(ByteString.copyFrom(iv));
		}
		
		return builder.build();
	}
}
