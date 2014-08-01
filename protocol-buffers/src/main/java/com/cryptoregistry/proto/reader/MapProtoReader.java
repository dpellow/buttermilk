/*
 *  This file is part of Buttermilk
 *  Copyright 2011-2014 David R. Smith All Rights Reserved.
 *
 */
package com.cryptoregistry.proto.reader;

import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import com.cryptoregistry.protos.Buttermilk.EntryProto;
import com.cryptoregistry.protos.Buttermilk.MapProto;

public class MapProtoReader {

	final MapProto proto;
	
	public MapProtoReader(MapProto proto) {
		this.proto = proto;
	}
	
	public Map<String,String> read(){
		LinkedHashMap<String,String> map = new LinkedHashMap<String,String>();
		List<EntryProto> list = proto.getEntriesList();
		Iterator<EntryProto> iter = list.iterator();
		while(iter.hasNext()){
			EntryProto entry = iter.next();
			String key = entry.getKey();
			String value = entry.getValue();
			map.put(key, value);
		}
		return map;
	}

}
