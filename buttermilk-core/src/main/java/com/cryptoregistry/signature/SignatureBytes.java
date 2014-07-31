/*
 *  This file is part of Buttermilk
 *  Copyright 2011-2014 David R. Smith All Rights Reserved.
 *
 */
package com.cryptoregistry.signature;

/**
 * Currently all the supported signature algorithms have either one or two parts as their output, 
 * each of which can be represented by a byte array. This interface is implemented by classes
 * for representation of all supported signature results
 * 
 * @author Dave
 *
 */
public interface SignatureBytes {

	public byte [] b1();
	public byte [] b2();
}
