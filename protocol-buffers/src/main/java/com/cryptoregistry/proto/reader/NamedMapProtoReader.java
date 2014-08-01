/*
 *  This file is part of Buttermilk
 *  Copyright 2011-2014 David R. Smith All Rights Reserved.
 *
 */
package com.cryptoregistry.proto.reader;

import java.util.Map;

import com.cryptoregistry.MapData;
import com.cryptoregistry.protos.Buttermilk.MapProto;
import com.cryptoregistry.protos.Buttermilk.NamedMapProto;

public class NamedMapProtoReader {
	
	final NamedMapProto proto;

	public NamedMapProtoReader(NamedMapProto proto) {
		this.proto = proto;
	}
	
	public MapData read() {
		MapProto mp = proto.getMap();
		MapProtoReader mpr = new MapProtoReader(mp);
		Map<String,String> map = mpr.read();
		String uuid = proto.getUuid();
		return new MapData(uuid,map);
	}

}
