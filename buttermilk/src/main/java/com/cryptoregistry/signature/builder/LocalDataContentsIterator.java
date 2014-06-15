package com.cryptoregistry.signature.builder;

import java.util.Iterator;

import com.cryptoregistry.LocalData;

public class LocalDataContentsIterator implements Iterator<String> {
	
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

	public String get(String key){
		if(key.startsWith("."))
		return localData.data.get(key.substring(1,key.length()));
		else return localData.data.get(key.substring(localData.uuid.length(),key.length()));
	}
	
	public String getHandle(){
		return localData.uuid;
	}

}
