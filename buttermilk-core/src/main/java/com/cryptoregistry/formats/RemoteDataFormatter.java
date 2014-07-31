package com.cryptoregistry.formats;

import java.io.IOException;
import java.io.Writer;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.cryptoregistry.ListData;
import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.core.JsonGenerator;

class RemoteDataFormatter {

	private List<ListData> listData;

	public RemoteDataFormatter() {
		super();
		listData = new ArrayList<ListData>();
	}

	public RemoteDataFormatter(List<ListData> listData) {
		super();
		this.listData = listData;
	}
	
	public void add(ListData rd){
		listData.add(rd);
	}

	public void format(JsonGenerator g, Writer writer) throws JsonGenerationException, IOException{
		Iterator<ListData>iter = listData.iterator();
		while(iter.hasNext()){
			ListData c = iter.next();
		//	g.writeStartArray();
			Iterator<String> inner = c.urls.iterator();
			while(inner.hasNext()){
				String url = inner.next();
				g.writeString(url);
			}
		//	g.writeEndArray();
		}
	}
}
