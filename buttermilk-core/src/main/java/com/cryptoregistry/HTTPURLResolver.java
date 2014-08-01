/*
 *  This file is part of Buttermilk
 *  Copyright 2011-2014 David R. Smith All Rights Reserved.
 *
 */
package com.cryptoregistry;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.databind.ObjectMapper;

public class HTTPURLResolver extends URLResolver {

	public HTTPURLResolver() {
		super();
	}

	public HTTPURLResolver(String url) {
		super(url);
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<MapData> resolve() {
		
		URLGrabber g = new URLGrabber(url);
		
		List<MapData> list = new ArrayList<MapData>();
		ObjectMapper mapper = new ObjectMapper();
		try {
			Map<String,Object> root = mapper.readValue(g.grab(), Map.class);
			Iterator<String> keys = root.keySet().iterator();
			while(keys.hasNext()){
				String key = keys.next();
				Map<String,String> attribs = (Map<String,String>) root.get(key);
				MapData ld = new MapData(key,attribs);
				list.add(ld);
			}
		} catch (Exception e) {
				throw new RuntimeException(e);
		}
		return list;
	}
	
	/**
	 * Trivial URL access helper class. A more serious implementation would use Apache HTTPClient, etc.
	 * I'm doing it this way to avoid unnecessary dependencies
	 * 
	 * TODO Implement it in a subclass by overriding resolve() above.
	 *
	 */
	
	private class URLGrabber {
		
		private String urlString;
		private StringBuffer output;
		private String LS = System.getProperty("line.separator");
		
		public URLGrabber(String urlStr) {
			urlString = urlStr;
			output = new StringBuffer();
		}

		public String grab() {
			try {
				URL url = new URL(urlString);
				
			    URLConnection con = url.openConnection();
			    BufferedReader in = new BufferedReader( new InputStreamReader(con.getInputStream(), "UTF-8"));
			    String inputLine=null;
		
			    while ((inputLine = in.readLine()) != null) {
			            output.append(inputLine);
			            output.append(LS);
			    }
		        in.close();
		        
			} catch (IOException e) {
				e.printStackTrace();
				throw new RuntimeException("URLGrabber failed...", e);
			}
			
			return output.toString();
		}
	}
}
