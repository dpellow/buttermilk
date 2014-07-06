package com.cryptoregistry.proto.builder;

import java.util.List;

import com.cryptoregistry.protos.Buttermilk.ListProto;

public class ListProtoBuilder {

	final List<String> list;
	
	public ListProtoBuilder(List<String> list) {
		this.list = list;
	}
	
	public ListProto build() {
		return ListProto.newBuilder().addAllList(list).build();
	}

}
