/*
 *  This file is part of Buttermilk
 *  Copyright 2011-2014 David R. Smith All Rights Reserved.
 *
 */
package com.cryptoregistry.proto.reader;

import java.util.List;

import com.cryptoregistry.protos.Buttermilk.*;

public class ListProtoReader {

	ListProto proto;
	
	public ListProtoReader(ListProto proto) {
		this.proto = proto;
	}
	
	public List<String> read() {
		return proto.getListList();
	}

}
