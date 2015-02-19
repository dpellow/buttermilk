/*
 *  This file is part of Buttermilk
 *  Copyright 2011-2014 David R. Smith All Rights Reserved.
 *
 */
package com.cryptoregistry.formats;

import java.io.IOException;
import java.io.Writer;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.cryptoregistry.CryptoContact;
import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.core.JsonGenerator;

class ContactFormatter {

	private List<CryptoContact> contacts;

	public ContactFormatter() {
		super();
		contacts = new ArrayList<CryptoContact>();
	}

	public ContactFormatter(List<CryptoContact> contacts) {
		super();
		this.contacts = contacts;
	}
	
	public void add(CryptoContact c){
		contacts.add(c);
	}

	public void format(JsonGenerator g, Writer writer) throws JsonGenerationException, IOException{
		Iterator<CryptoContact >iter = contacts.iterator();
		while(iter.hasNext()){
			CryptoContact c = iter.next();
			g.writeObjectFieldStart(c.getHandle());
			Iterator<String> inner = c.iterator();
			while(inner.hasNext()){
				String key = inner.next();
				if(key.equals("Handle")) continue;
				g.writeStringField(key, c.get(key));
			}
			g.writeEndObject();
		}
	}
	
	

}
