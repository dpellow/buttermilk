package com.cryptoregistry.formats;

import java.io.IOException;
import java.io.Writer;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.cryptoregistry.RemoteData;
import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.core.JsonGenerator;

class RemoteDataFormatter {

	private List<RemoteData> remoteData;

	public RemoteDataFormatter() {
		super();
		remoteData = new ArrayList<RemoteData>();
	}

	public RemoteDataFormatter(List<RemoteData> remoteData) {
		super();
		this.remoteData = remoteData;
	}
	
	public void add(RemoteData rd){
		remoteData.add(rd);
	}

	public void format(JsonGenerator g, Writer writer) throws JsonGenerationException, IOException{
		Iterator<RemoteData>iter = remoteData.iterator();
		while(iter.hasNext()){
			RemoteData c = iter.next();
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
