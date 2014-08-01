/*
 *  This file is part of Buttermilk
 *  Copyright 2011-2014 David R. Smith All Rights Reserved.
 *
 */
package com.cryptoregistry.proto.builder;

import java.util.Map;
import java.util.UUID;

import com.cryptoregistry.protos.Buttermilk.MapProto;
import com.cryptoregistry.protos.Buttermilk.NamedMapProto;

public class NamedMapProtoBuilder {

	final String uuid;
	final Map<String, String> map;

	public NamedMapProtoBuilder(Map<String, String> map) {
		this.map = map;
		uuid = UUID.randomUUID().toString();
	}
	
	public NamedMapProtoBuilder(String uuid, Map<String, String> map) {
		this.map = map;
		this.uuid = uuid;
	}

	public NamedMapProto build() {
		MapProtoBuilder mpBuilder = new MapProtoBuilder(map);
		MapProto mp = mpBuilder.build();
		return NamedMapProto.newBuilder().setUuid(uuid).setMap(mp).build();
	}

}
