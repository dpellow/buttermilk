/*
 *  This file is part of Buttermilk
 *  Copyright 2011-2014 David R. Smith All Rights Reserved.
 *
 */
package com.cryptoregistry.proto.reader;

import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

import com.cryptoregistry.crypto.mt.SecureMessage;
import com.cryptoregistry.crypto.mt.SecureMessageHeader;
import com.cryptoregistry.crypto.mt.Segment;
import com.cryptoregistry.proto.compat.InputTypeAdapter;
import com.cryptoregistry.protos.Buttermilk.SecureMessageProto;
import com.cryptoregistry.protos.Buttermilk.SegmentProto;

public class SecureMessageProtoReader {

	final SecureMessageProto proto;

	public SecureMessageProtoReader(SecureMessageProto proto) {
		super();
		this.proto = proto;
	}

	public SecureMessage read() {
		SecureMessageHeader header = new SecureMessageHeader(
				InputTypeAdapter.getInputTypefor(proto.getInputType()),
				Charset.forName(proto.getCharset()),
				proto.getIv().toByteArray());
		
		List<Segment> segs = new ArrayList<Segment>();
		for(SegmentProto sp: proto.getSegmentsList()) {
			segs.add(new Segment(sp.getData().toByteArray(),sp.getIv().toByteArray()));
		}
		return new SecureMessage(header,segs);
	}
}
