/*
 *  This file is part of Buttermilk
 *  Copyright 2011-2014 David R. Smith All Rights Reserved.
 *
 */
package com.cryptoregistry.proto.builder;

import com.cryptoregistry.crypto.mt.Segment;
import com.cryptoregistry.protos.Buttermilk.SegmentProto;
import com.google.protobuf.ByteString;

/**
 * Make a proto from a Segment
 * 
 * @author Dave
 *
 */
public class SegmentProtoBuilder {

	final byte [] data;
	final byte [] iv;
	
	public SegmentProtoBuilder(Segment segment) {
		this.data = segment.getOutput();
		this.iv = segment.getIv();
	}
	
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
