package com.cyptoregistry.formats;

import java.io.Reader;
import java.util.Map;

import com.cryptoregistry.passwords.Password;
import com.cryptoregistry.rsa.RSAKeyContents;
import com.fasterxml.jackson.databind.ObjectMapper;

public class JsonRSAFormatReader {

	protected final Map<String,String> in;
	
	@SuppressWarnings("unchecked")
	public JsonRSAFormatReader(Reader reader) {
		ObjectMapper mapper = new ObjectMapper();
		try {
			in = (Map<String, String>) mapper.readValue(reader, Map.class);
		} catch (Exception x) {
			throw new RuntimeException(x);
		}
	}
	
	/**
	 * Password may be null if there is certainty that the input will not need to be unencrypted
	 * 
	 * @param password
	 * @return
	 */
	public RSAKeyContents read(Password password) {
		
		return null;
	}

}
