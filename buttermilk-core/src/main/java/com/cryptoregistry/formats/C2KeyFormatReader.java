/*
 *  This file is part of Buttermilk
 *  Copyright 2011-2014 David R. Smith All Rights Reserved.
 *
 */
package com.cryptoregistry.formats;

import java.util.Date;
import java.util.Iterator;
import java.util.Map;

import com.cryptoregistry.c2.key.AgreementPrivateKey;
import com.cryptoregistry.c2.key.C2KeyMetadata;
import com.cryptoregistry.c2.key.Curve25519KeyContents;
import com.cryptoregistry.c2.key.PublicKey;
import com.cryptoregistry.c2.key.SigningPrivateKey;
import com.cryptoregistry.util.ArmoredString;
import com.cryptoregistry.util.TimeUtil;

/**
 * Given a map of key data in unlocked condition, create a C2 style key from that
 * 
 * @author Dave
 *
 */
public class C2KeyFormatReader {

	final Map<String,Object> map;
	
	public C2KeyFormatReader(Map<String,Object> map) {
		this.map = map;
	}
	
	@SuppressWarnings("unchecked")
	public Curve25519KeyContents read() {
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
				
				ArmoredString P = new ArmoredString(String.valueOf(inner.get("P")));
				ArmoredString k = new ArmoredString(String.valueOf(inner.get("k")));
				ArmoredString s = new ArmoredString(String.valueOf(inner.get("s")));
				
				C2KeyMetadata meta = new C2KeyMetadata(
						distUUID.substring(0,distUUID.length()-2),
						createdOn,
						new KeyFormat(enc,Mode.UNSECURED,null)
				);
				
				PublicKey pKey = new PublicKey(P.decodeToBytes());
				SigningPrivateKey sPrivKey = new SigningPrivateKey(s.decodeToBytes());
				AgreementPrivateKey aPrivKey = new AgreementPrivateKey(k.decodeToBytes());
				
				return new Curve25519KeyContents(meta,pKey,sPrivKey,aPrivKey);
				
			}else{
				throw new RuntimeException("unexpected Mode, needs to be Unsecure");
			}
		}else{
			throw new RuntimeException("Count not find the uuid, fail");
		}
	}

}
