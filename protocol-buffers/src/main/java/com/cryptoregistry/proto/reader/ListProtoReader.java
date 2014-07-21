package com.cryptoregistry.proto.reader;

import java.util.List;

import com.cryptoregistry.protos.Buttermilk.ListProto;

public class ListProtoReader {

	ListProto proto;
	
	public ListProtoReader(ListProto proto) {
		this.proto = proto;
	}
	
	public List<String> read() {
		return proto.getListList();
	}

}
