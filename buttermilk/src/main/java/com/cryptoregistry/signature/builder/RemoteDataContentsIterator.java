package com.cryptoregistry.signature.builder;

import java.io.File;
import java.util.Iterator;
import java.util.List;

import com.cryptoregistry.FileURLResolver;
import com.cryptoregistry.HTTPURLResolver;
import com.cryptoregistry.LocalData;
import com.cryptoregistry.RemoteData;

public class RemoteDataContentsIterator implements Iterator<String> {
	
	final RemoteData remoteData;
	private Iterator<String> iter;
	int index = 0;

	public RemoteDataContentsIterator(RemoteData rd) {
		super();
		this.remoteData = rd;
		iter = remoteData.urls.iterator();
	}

	@Override
	public boolean hasNext() {
		return iter.hasNext();
	}

	@Override
	public String next() {
		return iter.next();
	}

	@Override
	public void remove() {
		iter.remove();
	}

	public List<LocalData> nextData(){
		
		String url = next();
		String three = url.substring(0, 3).toUpperCase(); // first 3 chars
		switch(three){
			case "FIL" : {
				FileURLResolver fRes = new FileURLResolver(url);
				return fRes.resolve();
			}
			case "HTT" : {
				HTTPURLResolver hRes = new HTTPURLResolver(url);
				return hRes.resolve();
			}
			
			// ...
			
			default: {
				// try file
				File testF = new File(url);
				if(testF.exists()){
					FileURLResolver fRes = new FileURLResolver(url);
					return fRes.resolve();
				}
			}
		}
		
		throw new RuntimeException("Problem: should not have reached this point.");
	}

}
