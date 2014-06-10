package com.cryptoregistry.signature.builder;

import java.util.Iterator;

import com.cryptoregistry.CryptoContact;

public class ContactContentsIterator implements Iterator<String> {
	
	final CryptoContact contact;
	private Iterator<String> iter;

	private ContactContentsIterator(CryptoContact contact) {
		super();
		this.contact = contact;
		iter = contact.getMap().keySet().iterator();
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

	public String get(String key){
		return contact.getMap().get(key);
	}
	
	public void reset(){
		if(!iter.hasNext()) iter = contact.getMap().keySet().iterator();
	}

}
