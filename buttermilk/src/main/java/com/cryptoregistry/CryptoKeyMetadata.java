package com.cryptoregistry;

import java.util.Date;

import com.cryptoregistry.formats.KeyFormat;

/**
 * Marker interface for things all key materials should have
 * 
 * @author Dave
 *
 */
public interface CryptoKeyMetadata {

	String getHandle();
	String getKeyAlgorithm();
	Date getCreatedOn();
	KeyFormat getFormat();

}
