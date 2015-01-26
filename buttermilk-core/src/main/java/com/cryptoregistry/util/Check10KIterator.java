/*
 *  This file is part of Buttermilk
 *  Copyright 2011-2015 David R. Smith All Rights Reserved.
 *
 */
package com.cryptoregistry.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Iterator;


public class Check10KIterator implements Iterator<String> {
	
	ArrayList<String> list = new ArrayList<String>();
	int index = 0;

	public Check10KIterator() {
		InputStream in = Thread.currentThread().getClass()
				.getResourceAsStream("/10k most common.txt");
		InputStreamReader reader = new InputStreamReader(in);
		BufferedReader bin = new BufferedReader(reader);
		String line = null;
		try {
			while ((line = bin.readLine()) != null) {
				String l = line.trim();
				list.add(l);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Override
	public boolean hasNext() {
		if(index < list.size()){
			return true;
		}
		return false;
	}

	@Override
	public String next() {
		String item = list.get(index);
		index++;
		return item;
	}

	@Override
	public void remove() {
		throw new RuntimeException("Not implemented");
	}

}
