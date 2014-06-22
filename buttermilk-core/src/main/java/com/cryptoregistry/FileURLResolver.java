package com.cryptoregistry;

import java.io.File;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.databind.ObjectMapper;

public class FileURLResolver extends URLResolver {

	public FileURLResolver() {
		super();
	}

	public FileURLResolver(String url) {
		super(url);
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<LocalData> resolve() {
		URL obj;
		try {
			obj = new URL(url);
		} catch (Exception e1) {
			throw new RuntimeException(e1);
		}
		File f;
		try {
		  f = new File(obj.toURI());
		} catch(URISyntaxException e) {
		  f = new File(obj.getPath());
		}
			
		List<LocalData> list = new ArrayList<LocalData>();
		ObjectMapper mapper = new ObjectMapper();
		try {
			Map<String,Object> root = mapper.readValue(f, Map.class);
			Iterator<String> keys = root.keySet().iterator();
			while(keys.hasNext()){
				String key = keys.next();
				Map<String,Object> attribs = (Map<String,Object>) root.get(key);
				LocalData ld = new LocalData(key,attribs);
				list.add(ld);
			}
		} catch (Exception e) {
				throw new RuntimeException(e);
		}
		return list;
	}
}
