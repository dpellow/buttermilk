/*
 *  This file is part of Buttermilk
 *  Copyright 2011-2014 David R. Smith All Rights Reserved.
 *
 */
package com.cryptoregistry.proto.builder;

import java.util.Iterator;
import java.util.Map;

import com.cryptoregistry.protos.Buttermilk.EntryProto;
import com.cryptoregistry.protos.Buttermilk.MapProto;

public class MapProtoBuilder {

	final Map<String, String> map;

	public MapProtoBuilder(Map<String, String> map) {
		this.map = map;
	}

	public MapProto build() {
		// create the map proto

		MapProto.Builder mapProtoBuilder = MapProto.newBuilder();
		Iterator<String> iter = map.keySet().iterator();
		while (iter.hasNext()) {
			String key = iter.next();
			String value = map.get(key);
			EntryProto entry = EntryProto.newBuilder().setKey(key)
					.setValue(value).build();
			mapProtoBuilder.addEntries(entry);
		}

		return mapProtoBuilder.build();
	}

}
