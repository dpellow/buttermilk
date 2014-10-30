/*
 *  This file is part of Buttermilk
 *  Copyright 2011-2014 David R. Smith All Rights Reserved.
 *
 */
package com.cryptoregistry.proto.reader;

import com.cryptoregistry.crypto.mt.Segment;
import com.cryptoregistry.protos.Buttermilk.SegmentProto;

public class SegmentProtoReader {

	final SegmentProto proto;

	public SegmentProtoReader(SegmentProto proto) {
		super();
		this.proto = proto;
	}

	public Segment read() {
		return new Segment(proto.getData().toByteArray(), proto.getIv().toByteArray());
	}
}
