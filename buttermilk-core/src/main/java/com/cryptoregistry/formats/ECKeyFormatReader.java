package com.cryptoregistry.formats;

import java.math.BigInteger;
import java.util.Date;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;

import x.org.bouncycastle.math.ec.ECPoint;

import com.cryptoregistry.ec.ECCustomParameters;
import com.cryptoregistry.ec.ECF2MCustomParameters;
import com.cryptoregistry.ec.ECFPCustomParameters;
import com.cryptoregistry.ec.ECKeyContents;
import com.cryptoregistry.ec.ECKeyMetadata;
import com.cryptoregistry.util.TimeUtil;

/**
 * This class is used to read the contents of the ArmoredPBE
 * 
 * @author Dave
 * 
 */
public class ECKeyFormatReader {

	final Map<String, Object> map;

	public ECKeyFormatReader(Map<String, Object> map) {
		this.map = map;
	}

	@SuppressWarnings("unchecked")
	public ECKeyContents read() {
		Iterator<String> iter = map.keySet().iterator();
		if (iter.hasNext()) {
			String distUUID = iter.next();
			Map<String, Object> inner = (Map<String, Object>) map.get(distUUID);
			if (distUUID.endsWith("-U")) {
				Date createdOn = TimeUtil.getISO8601FormatDate(String.valueOf(inner.get("CreatedOn")));
				
				String curveName = null;
				ECCustomParameters params = null;
				
				if(inner.containsKey("CurveName")) {
					curveName = String.valueOf(inner.get("CurveName"));
				} else {
					// custom curve definition
					// first get the parameters into a map we can pass into the constructor
					Map<String,Object> def = (Map<String,Object>) inner.get("Curve");
					LinkedHashMap<String,String> map = new LinkedHashMap<String,String>();
					Iterator<String> _iter = def.keySet().iterator();
					while(_iter.hasNext()){
						String key = _iter.next();
						map.put(key, String.valueOf(def.get(key)));
					}
					
					if(def.get("Field").equals("FP")){
						params = new ECFPCustomParameters(null,map);
					}else if(def.get("Field").equals("F2M")){
						params = new ECF2MCustomParameters(null,map);
					}
				}
				
				EncodingHint enc = EncodingHint.valueOf(String.valueOf(inner.get("Encoding")));
				ECPoint Q = null;
				if(curveName != null) {
					Q = FormatUtil.parseECPoint(curveName, enc, String.valueOf(inner.get("Q")));
				}else{
					Q = FormatUtil.parseECPoint(params.getParameters().getCurve(), enc, String.valueOf(inner.get("Q")));
				}
				BigInteger D = FormatUtil.unwrap(enc, String.valueOf(inner.get("D")));

				ECKeyMetadata meta = new ECKeyMetadata(distUUID.substring(0,
						distUUID.length() - 2), createdOn, new KeyFormat(enc,
						Mode.UNSECURED, null));

				return new ECKeyContents(meta, Q, curveName, D);

			} else {
				throw new RuntimeException(
						"unexpected Mode, needs to be Unsecure");
			}
		} else {
			throw new RuntimeException("Count not find the uuid, fail");
		}
	}
}
