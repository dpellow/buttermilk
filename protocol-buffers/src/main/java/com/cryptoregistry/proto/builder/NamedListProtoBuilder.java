/*
 *  This file is part of Buttermilk
 *  Copyright 2011-2014 David R. Smith All Rights Reserved.
 *
 */
package com.cryptoregistry.proto.builder;

import java.util.List;
import java.util.UUID;

import com.cryptoregistry.protos.Buttermilk.ListProto;
import com.cryptoregistry.protos.Buttermilk.NamedListProto;

public class NamedListProtoBuilder {

	final String uuid;
	final List<String> list;
	
	public NamedListProtoBuilder(List<String> list) {
		this.list = list;
		uuid = UUID.randomUUID().toString();
	}
	
	public NamedListProtoBuilder(String uuid, List<String> list) {
		this.list = list;
		this.uuid = uuid;
	}
	
	public NamedListProto build() {
		ListProto l = ListProto.newBuilder().addAllList(list).build();
		NamedListProto proto = NamedListProto.newBuilder().setUuid(uuid).setMap(l).build();
		return proto;
	}

}
