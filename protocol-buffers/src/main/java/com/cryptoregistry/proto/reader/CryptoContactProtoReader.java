/*
 *  This file is part of Buttermilk
 *  Copyright 2011-2014 David R. Smith All Rights Reserved.
 *
 */
package com.cryptoregistry.proto.reader;


import java.util.LinkedHashMap;
import java.util.ListIterator;
import java.util.Map;

import com.cryptoregistry.CryptoContact;
import com.cryptoregistry.protos.Buttermilk.CryptoContactProto;
import com.cryptoregistry.protos.Buttermilk.EntryProto;
import com.cryptoregistry.protos.Buttermilk.MapProto;

public class CryptoContactProtoReader {

	final CryptoContactProto proto;

	public CryptoContactProtoReader(CryptoContactProto proto) {
		super();
		this.proto = proto;
	}
	
	public CryptoContact read() {
		String uuid = proto.getMap().getUuid();
		Map<String,Object> map = new LinkedHashMap<String,Object>();
		MapProto mapProto = proto.getMap().getMap();
		ListIterator<EntryProto> iter = mapProto.getEntriesList().listIterator();
		while(iter.hasNext()) {
			EntryProto ep = iter.next();
			String key = ep.getKey();
			String value = ep.getValue();
			map.put(key, value);
		}
		return new CryptoContact(uuid,map);
	} 

}
