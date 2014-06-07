package com.cryptoregistry;

import java.util.Date;

import com.cryptoregistry.formats.KeyFormat;

/**
 * Metadata includes things like a handle
 * 
 * @author Dave
 *
 */
public interface CryptoKeyMetadata {

	String getHandle();
	KeyGenerationAlgorithm getKeyAlgorithm();
	Date getCreatedOn();
	KeyFormat getFormat();

}
