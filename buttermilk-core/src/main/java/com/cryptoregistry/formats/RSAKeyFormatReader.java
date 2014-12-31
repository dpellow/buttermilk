/*
 *  This file is part of Buttermilk
 *  Copyright 2011-2014 David R. Smith All Rights Reserved.
 *
 */
package com.cryptoregistry.formats;

import java.math.BigInteger;
import java.util.Date;
import java.util.Iterator;
import java.util.Map;

import com.cryptoregistry.rsa.RSAKeyContents;
import com.cryptoregistry.rsa.RSAKeyMetadata;
import com.cryptoregistry.util.TimeUtil;

/**
 * This class is used to read the contents of the ArmoredPBE
 * 
 * @author Dave
 * 
 */
public class RSAKeyFormatReader {

	final Map<String, Object> map;

	public RSAKeyFormatReader(Map<String, Object> map) {
		this.map = map;
	}

	@SuppressWarnings("unchecked")
	public RSAKeyContents read() {
		Iterator<String> iter = map.keySet().iterator();
		if (iter.hasNext()) {
			String distUUID = iter.next();
			Map<String, Object> inner = (Map<String, Object>) map.get(distUUID);
			if (distUUID.endsWith("-U")) {
				Date createdOn = TimeUtil.getISO8601FormatDate(String
						.valueOf(inner.get("CreatedOn")));
				
				EncodingHint enc = EncodingHint.valueOf(String.valueOf(inner.get("Encoding")));
				int strength = Integer.parseInt(String.valueOf(inner.get("Strength")));
				BigInteger Modulus = FormatUtil.unwrap(enc, String.valueOf(inner.get("Modulus")));
				BigInteger publicExponent = FormatUtil.unwrap(enc, String.valueOf(inner.get("PublicExponent")));
				BigInteger privateExponent = FormatUtil.unwrap(enc, String.valueOf(inner.get("PrivateExponent")));
				BigInteger P = FormatUtil.unwrap(enc, String.valueOf(inner.get("P")));
				BigInteger Q = FormatUtil.unwrap(enc, String.valueOf(inner.get("Q")));
				BigInteger dP = FormatUtil.unwrap(enc, String.valueOf(inner.get("dP")));
				BigInteger dQ = FormatUtil.unwrap(enc, String.valueOf(inner.get("dQ")));
				BigInteger qInv = FormatUtil.unwrap(enc, String.valueOf(inner.get("qInv")));

				RSAKeyMetadata meta = new RSAKeyMetadata(
						distUUID.substring(0, distUUID.length() - 2), 
						createdOn, 
						new KeyFormat(enc, Mode.UNSECURED, null)
				);
				
				meta.setStrength(strength);

				return new RSAKeyContents(meta, Modulus,publicExponent,privateExponent,P,Q,dP,dQ,qInv);

			} else {
				throw new RuntimeException(
						"unexpected Mode, needs to be Unsecure");
			}
		} else {
			throw new RuntimeException("Count not find the uuid, fail");
		}
	}
}
