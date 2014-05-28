package com.cyptoregistry.formats;

import java.io.IOException;
import java.math.BigInteger;

import net.iharder.Base64;

public class FormatUtil {
	
	public static String wrap(Encoding enc, BigInteger bi) {
		switch (enc) {
		case Base2:
			return bi.toString(2);
		case Base10:
			return bi.toString(10);
		case Base16:
			return bi.toString(16);
		case Base64: {
			return Base64.encodeBytes(bi.toByteArray());
		}
		case Base64url: {
			try {
				return Base64.encodeBytes(bi.toByteArray(), Base64.URL_SAFE);
			} catch (IOException e) {
				throw new RuntimeException(e);
			}
		}
		default:
			throw new RuntimeException("Unknown encoding: " + enc);
		}
	}

	public static BigInteger unwrap(Encoding enc, String s) {
		switch (enc) {
		case Base2:
			return new BigInteger(s,2);
		case Base10:
			return new BigInteger(s,10);
		case Base16:
			return new BigInteger(s,16);
		case Base64: {
			try {
				byte [] b = Base64.decode(s);
				return new BigInteger(b);
			} catch (IOException e) {
				throw new RuntimeException(e);
			}
		}
		case Base64url: {
			try {
				byte [] b = Base64.decode(s, Base64.URL_SAFE);
				return new BigInteger(b);
			} catch (IOException e) {
				throw new RuntimeException(e);
			}
		}
		default:
			throw new RuntimeException("Unknown encoding: " + enc);
		}
	}

}
