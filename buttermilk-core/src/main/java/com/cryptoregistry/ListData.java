/*
 *  This file is part of Buttermilk
 *  Copyright 2011-2014 David R. Smith All Rights Reserved.
 *
 */
package com.cryptoregistry;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class ListData {

	public final String uuid;
	public final List<String> urls;
	
	public ListData() {
		super();
		urls = new ArrayList<String>();
		this.uuid = UUID.randomUUID().toString();
	}
	
	public ListData(String uuid) {
		super();
		urls = new ArrayList<String>();
		this.uuid = uuid;
	}
	
	public ListData(String uuid, List<String> list) {
		super();
		urls = list;
		this.uuid = uuid;
	}
	
	public void addURL(String url){
		urls.add(url);
	}
	
	public void addFile(File path){
		try {
			urls.add(path.toURI().toURL().toString());
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void addPath(Path path){
		addFile(path.toFile());
	}
	
	public void addURL(URL url){
		addURL(url.toString());
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((urls == null) ? 0 : urls.hashCode());
		result = prime * result + ((uuid == null) ? 0 : uuid.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		ListData other = (ListData) obj;
		if (urls == null) {
			if (other.urls != null)
				return false;
		} else if (!urls.equals(other.urls))
			return false;
		if (uuid == null) {
			if (other.uuid != null)
				return false;
		} else if (!uuid.equals(other.uuid))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "RemoteData [uuid=" + uuid + ", urls=" + urls + "]";
	}

}
