package com.cryptoregistry.formats;

import java.io.IOException;
import java.io.Writer;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.cryptoregistry.LocalData;
import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.core.JsonGenerator;

class LocalDataFormatter {

	private List<LocalData> localData;

	public LocalDataFormatter() {
		super();
		localData = new ArrayList<LocalData>();
	}

	public LocalDataFormatter(List<LocalData> localData) {
		super();
		this.localData = localData;
	}
	
	public void add(LocalData ld){
		localData.add(ld);
	}

	public void format(JsonGenerator g, Writer writer) throws JsonGenerationException, IOException{
		Iterator<LocalData>iter = localData.iterator();
		while(iter.hasNext()){
			LocalData c = iter.next();
			g.writeObjectFieldStart(c.uuid);
			Iterator<String> inner = c.data.keySet().iterator();
			while(inner.hasNext()){
				String key = inner.next();
				g.writeStringField(key, c.data.get(key));
			}
			g.writeEndObject();
		}
	}
}
