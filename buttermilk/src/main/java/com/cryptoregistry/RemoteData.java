package com.cryptoregistry;

import java.util.ArrayList;
import java.util.List;

public class RemoteData {

	public final List<String> urls;
	
	public RemoteData() {
		super();
		urls = new ArrayList<String>();
	}
	
	public void addURL(String url){
		urls.add(url);
	}

}
