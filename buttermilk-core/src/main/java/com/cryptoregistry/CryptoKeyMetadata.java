/*
 *  This file is part of Buttermilk
 *  Copyright 2011-2014 David R. Smith All Rights Reserved.
 *
 */
package com.cryptoregistry;

import java.util.Date;

import com.cryptoregistry.formats.KeyFormat;

/**
 * Metadata includes things like a handle and the key generation algorithm
 * 
 * @author Dave
 *
 */
public interface CryptoKeyMetadata {

	/**
	 * A handle is a String token which is unique within the problem domain, and which conforms 
	 * to certain rules not defined here; it is used to manipulate the set of attributes 
	 * associated with a key, for example when performing a digital signature
	 * 
	 * @return
	 */
	String getHandle();
	
	/**
	 * The KeyAlgorithm is the asymmetric algorithm used to generate the key. These typically conform
	 * the the Java JCE "KeyPairGenerator" Standard Names as defined  in 
	 * http://docs.oracle.com/javase/6/docs/technotes/guides/security/StandardNames.html#KeyPairGenerator, 
	 * but when a standard name is not defined, e.g., Curve25519, then we make up our own. 
	 * 
	 * @return
	 */
	KeyGenerationAlgorithm getKeyAlgorithm();
	
	/**
	 * When the key materials are initially defined
	 * 
	 * @return
	 */
	Date getCreatedOn();
	
	/**
	 * KeyFormat provides details on Mode, Encoding, and PBEParams (if the mode is 'Secure') 
	 * @return
	 */
	KeyFormat getFormat();
	
	/**
	 * Distinguished handle is a construct of the handle (typically a UUID) appended with the key Mode 
	 * (a code which indicates Secure, Unsecure, or ForPublication). This allows for easy determination
	 * of the nature of the key mode without having to parse the attributes; it also allows for the
	 * presence of more than one mode of a key in the same file without duplication
	 * 
	 * @return
	 */
	String getDistinguishedHandle();

}
