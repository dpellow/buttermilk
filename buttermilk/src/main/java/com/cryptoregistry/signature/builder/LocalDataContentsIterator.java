package com.cryptoregistry.signature.builder;

import java.util.Iterator;

import com.cryptoregistry.LocalData;
import com.cryptoregistry.util.MapIterator;

public class LocalDataContentsIterator implements MapIterator {
	
	final LocalData localData;
	private Iterator<String> iter;
	int index = 0;

	public LocalDataContentsIterator(LocalData ld) {
		super();
		this.localData = ld;
		iter = localData.data.keySet().iterator();
	}

	@Override
	public boolean hasNext() {
		return iter.hasNext();
	}

	@Override
	public String next() {
		if(index == 0){
			index++;
			return localData.uuid+":"+iter.next();
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
		return String.valueOf(localData.data.get(key.substring(1,key.length())));
		else return String.valueOf(localData.data.get(key.substring(localData.uuid.length()+1,key.length())));
	}
	
	public String getHandle(){
		return localData.uuid;
	}

}
