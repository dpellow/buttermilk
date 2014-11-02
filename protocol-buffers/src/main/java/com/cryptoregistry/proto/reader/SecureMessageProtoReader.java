/*
 *  This file is part of Buttermilk
 *  Copyright 2011-2014 David R. Smith All Rights Reserved.
 *
 */
package com.cryptoregistry.proto.reader;

import java.util.ArrayList;
import java.util.List;

import com.cryptoregistry.crypto.mt.SecureMessage;
import com.cryptoregistry.crypto.mt.SecureMessageHeader;
import com.cryptoregistry.crypto.mt.Segment;
import com.cryptoregistry.protos.Buttermilk.BytesProto;
import com.cryptoregistry.protos.Buttermilk.SecureBytesProto;

public class SecureMessageProtoReader {

	final SecureBytesProto proto;

	public SecureMessageProtoReader(SecureBytesProto proto) {
		super();
		this.proto = proto;
	}

	public SecureMessage read() {
		SecureMessageHeader header = new SecureMessageHeader(
				proto.getIv().toByteArray());
		
		System.err.println("segments:"+proto.getSegmentsCount());
		List<Segment> segs = new ArrayList<Segment>();
		for(BytesProto sp: proto.getSegmentsList()) {
			segs.add(new Segment(sp.getData().toByteArray()));
		}
		return new SecureMessage(header,segs);
	}
}
