/*
 *  This file is part of Buttermilk
 *  Copyright 2011-2014 David R. Smith All Rights Reserved.
 *
 */
package com.cryptoregistry.formats;

import java.util.Date;
import java.util.Iterator;
import java.util.Map;

import com.cryptoregistry.symmetric.SymmetricKeyContents;
import com.cryptoregistry.symmetric.SymmetricKeyMetadata;
import com.cryptoregistry.util.ArmoredString;
import com.cryptoregistry.util.TimeUtil;

/**
 * Given a map of key data in unlocked condition, create a Symmetric style key from that
 * 
 * @author Dave
 *
 */
public class SymmetricKeyFormatReader {

	final Map<String,Object> map;
	
	public SymmetricKeyFormatReader(Map<String,Object> map) {
		this.map = map;
	}
	
	@SuppressWarnings("unchecked")
	public SymmetricKeyContents read() {
		Iterator<String> iter = map.keySet().iterator();
		if(iter.hasNext()){
			String distUUID = iter.next();
			Map<String,Object> inner = (Map<String,Object>) map.get(distUUID);
			if(distUUID.endsWith("-U")){
			//	String keyAlgorithm = String.valueOf(inner.get("KeyAlgorithm"));
				Date createdOn = TimeUtil.getISO8601FormatDate(String.valueOf(inner.get("CreatedOn")));
				
				// at the moment these are always base64url
				EncodingHint enc =	EncodingHint.valueOf(String.valueOf(inner.get("Encoding")));
				if(enc != EncodingHint.Base64url) throw new RuntimeException("Unexpected encoding, needs to be Base64url");
				
				ArmoredString s = new ArmoredString(String.valueOf(inner.get("s")));
				
				SymmetricKeyMetadata meta = new SymmetricKeyMetadata(
						distUUID.substring(0,distUUID.length()-2),
						createdOn,
						new KeyFormat(enc,Mode.UNSECURED,null)
				);
				
				return new SymmetricKeyContents(meta,s.decodeToBytes());
				
			}else{
				throw new RuntimeException("unexpected Mode, needs to be Unsecure");
			}
		}else{
			throw new RuntimeException("Count not find the uuid, fail");
		}
	}

}
