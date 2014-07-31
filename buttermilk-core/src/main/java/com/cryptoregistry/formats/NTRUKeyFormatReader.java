/*
 *  This file is part of Buttermilk
 *  Copyright 2011-2014 David R. Smith All Rights Reserved.
 *
 */
package com.cryptoregistry.formats;

import java.util.Date;
import java.util.Iterator;
import java.util.Map;

import x.org.bouncycastle.pqc.crypto.ntru.NTRUEncryptionParameters;
import x.org.bouncycastle.pqc.math.ntru.polynomial.IntegerPolynomial;
import x.org.bouncycastle.pqc.math.ntru.polynomial.Polynomial;

import com.cryptoregistry.ntru.NTRUKeyContents;
import com.cryptoregistry.ntru.NTRUKeyMetadata;
import com.cryptoregistry.ntru.NTRUNamedParameters;
import com.cryptoregistry.ntru.PolynomialAdapter;
import com.cryptoregistry.util.TimeUtil;

/**
 * This class is used to read the contents of the ArmoredPBE for an NTRU Key
 * 
 * @author Dave
 * 
 */
public class NTRUKeyFormatReader {

	final Map<String, Object> map;

	public NTRUKeyFormatReader(Map<String, Object> map) {
		this.map = map;
	}
	
	@SuppressWarnings("unchecked")
	public NTRUKeyContents read() {
		Iterator<String> iter = map.keySet().iterator();
		if (iter.hasNext()) {
			String distUUID = iter.next();
			Map<String, Object> inner = (Map<String, Object>) map.get(distUUID);
			if (distUUID.endsWith("-U")) {
				Date createdOn = TimeUtil.getISO8601FormatDate(String.valueOf(inner.get("CreatedOn")));
				EncodingHint enc = EncodingHint.valueOf(String.valueOf(inner.get("Encoding")));
				NTRUKeyMetadata meta = new NTRUKeyMetadata(distUUID.substring(0,
						distUUID.length() - 2), createdOn, new KeyFormat(enc,
						Mode.UNSECURED, null));
				
				String _h = String.valueOf(inner.get("h"));
				String _fp = String.valueOf(inner.get("fp"));
				
				IntegerPolynomial h = PolynomialAdapter.unwrapIntegerPolynomial(_h);
				IntegerPolynomial fp = PolynomialAdapter.unwrapIntegerPolynomial(_fp);
				
				// find t
				String _t0, _t1, _t2, _ts, _td;
				
				Polynomial t = null;
				
				if(inner.containsKey("t0")) {
					_t0 = String.valueOf(inner.get("t0"));
					_t1 = String.valueOf(inner.get("t1"));
					_t2 = String.valueOf(inner.get("t2"));
					t = PolynomialAdapter.unwrapProductForm(_t0, _t1, _t2);
				}
				
				if(inner.containsKey("ts")) {
					_ts = String.valueOf(inner.get("ts"));
					t = PolynomialAdapter.unwrapSparse(_ts);
				}
				
				if(inner.containsKey("td")) {
					_td = String.valueOf(inner.get("td"));
					t = PolynomialAdapter.unwrapDense(_td);
				}
				
				String namedParameters = null;
				NTRUEncryptionParameters params = null;
				
				if(inner.containsKey("NamedParameters")){
					namedParameters = (String) inner.get("NamedParameters");
					NTRUNamedParameters np = NTRUNamedParameters.valueOf(namedParameters);
					return new NTRUKeyContents(meta,np,h,t,fp);
				}else{
					NTRUParametersReader reader = new NTRUParametersReader((Map<String, Object>)inner.get("NTRUParams"));
					params = reader.parse();
					return new NTRUKeyContents(meta,params,h,t,fp);
				}
				
				
				

			} else {
				throw new RuntimeException(
						"unexpected Mode, needs to be Unsecure");
			}
		} else {
			throw new RuntimeException("Count not find the uuid, fail");
		}
	}

}
