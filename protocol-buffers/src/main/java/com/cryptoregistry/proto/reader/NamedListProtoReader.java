/*
 *  This file is part of Buttermilk
 *  Copyright 2011-2014 David R. Smith All Rights Reserved.
 *
 */
package com.cryptoregistry.proto.reader;

import java.util.List;

import com.cryptoregistry.ListData;
import com.cryptoregistry.protos.Buttermilk.ListProto;
import com.cryptoregistry.protos.Buttermilk.NamedListProto;

public class NamedListProtoReader {

	final NamedListProto proto;
	
	public NamedListProtoReader(NamedListProto proto) {
		this.proto = proto;
	}
	
	public ListData read() {
		ListProto lp = proto.getMap();
		ListProtoReader lpr = new ListProtoReader(lp);
		List<String> list = lpr.read();
		String uuid = proto.getUuid();
		return new ListData(uuid,list);
	}

}
