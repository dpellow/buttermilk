package com.cryptoregistry;

import java.util.Date;

import com.cryptoregistry.formats.KeyFormat;

/**
 * Marker interface for things all keys should have
 * 
 * @author Dave
 *
 */
public interface CryptoKey {

	String getHandle();
	String getKeyAlgorithm();
	Date getCreatedOn();
	KeyFormat getFormat();

}
