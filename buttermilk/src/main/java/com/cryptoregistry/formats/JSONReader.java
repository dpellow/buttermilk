package com.cryptoregistry.formats;

import java.io.File;
import java.util.Map;

import com.cryptoregistry.KeyMaterials;
import com.fasterxml.jackson.databind.ObjectMapper;

public class JSONReader {
	
	protected final ObjectMapper mapper;
	protected final Map<String,Object> map;
	
	/**
	 * Fails immediately if anything goes wrong reading the file or parsing
	 * @param path
	 */
	@SuppressWarnings("unchecked")
	public JSONReader(File path) {
		
		// TODO use the parsing API
		mapper = new ObjectMapper();
		try {
			map = mapper.readValue(path, Map.class);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
	
	public KeyMaterials parse() {
		
		return null;
	}

}
