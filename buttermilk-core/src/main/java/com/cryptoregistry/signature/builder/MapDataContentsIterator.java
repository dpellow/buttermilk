/*
 *  This file is part of Buttermilk
 *  Copyright 2011-2014 David R. Smith All Rights Reserved.
 *
 */
package com.cryptoregistry.signature.builder;

import java.util.Iterator;

import com.cryptoregistry.MapData;
import com.cryptoregistry.util.MapIterator;

public class MapDataContentsIterator implements MapIterator {
	
	final MapData mapData;
	private Iterator<String> iter;
	int index = 0;

	public MapDataContentsIterator(MapData ld) {
		super();
		this.mapData = ld;
		iter = mapData.data.keySet().iterator();
	}

	@Override
	public boolean hasNext() {
		return iter.hasNext();
	}

	@Override
	public String next() {
		if(index == 0){
			index++;
			return mapData.uuid+":"+iter.next();
		}else{
			index++;
			return "."+iter.next();
		}
	}

	@Override
	public void remove() {
		iter.remove();
	}

	@Override
	public String get(String key){
		if(key.startsWith("."))
		return String.valueOf(mapData.data.get(key.substring(1,key.length())));
		else return String.valueOf(mapData.data.get(key.substring(mapData.uuid.length()+1,key.length())));
	}
	
	public String getHandle(){
		return mapData.uuid;
	}

}
