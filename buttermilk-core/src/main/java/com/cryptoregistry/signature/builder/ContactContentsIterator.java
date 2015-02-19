/*
 *  This file is part of Buttermilk
 *  Copyright 2011-2014 David R. Smith All Rights Reserved.
 *
 */
package com.cryptoregistry.signature.builder;

import java.util.Iterator;

import com.cryptoregistry.CryptoContact;
import com.cryptoregistry.util.MapIterator;

public class ContactContentsIterator implements MapIterator {
	
	final CryptoContact contact;
	private Iterator<String> iter;
	int index = 0;
	int handleLength;

	public ContactContentsIterator(CryptoContact contact) {
		super();
		this.contact = contact;
		iter = contact.iterator();
		handleLength = contact.getHandle().length()+1;
	}
	
	public String getHandle() {
		return contact.getHandle();
	}

	@Override
	public boolean hasNext() {
		return iter.hasNext();
	}

	@Override
	public String next() {
		if(index == 0) {
			index++;
			return getHandle()+":"+iter.next();
		}
		else {
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
		return contact.get(key.substring(1,key.length()));
		else return contact.get(key.substring(handleLength,key.length()));
	}
	
	public void reset(){
		if(!iter.hasNext()) {
			index = 0;
			iter = contact.iterator();
		}
	}

}
