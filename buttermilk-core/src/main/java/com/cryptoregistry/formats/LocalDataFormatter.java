package com.cryptoregistry.formats;

import java.io.IOException;
import java.io.Writer;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.cryptoregistry.MapData;
import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.core.JsonGenerator;

class LocalDataFormatter {

	private List<MapData> mapData;

	public LocalDataFormatter() {
		super();
		mapData = new ArrayList<MapData>();
	}

	public LocalDataFormatter(List<MapData> mapData) {
		super();
		this.mapData = mapData;
	}
	
	public void add(MapData ld){
		mapData.add(ld);
	}

	public void format(JsonGenerator g, Writer writer) throws JsonGenerationException, IOException{
		Iterator<MapData>iter = mapData.iterator();
		while(iter.hasNext()){
			MapData c = iter.next();
			g.writeObjectFieldStart(c.uuid);
			Iterator<String> inner = c.data.keySet().iterator();
			while(inner.hasNext()){
				String key = inner.next();
				g.writeStringField(key, String.valueOf(c.data.get(key)));
			}
			g.writeEndObject();
		}
	}
}
