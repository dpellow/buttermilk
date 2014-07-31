/*
 *  This file is part of Buttermilk
 *  Copyright 2011-2014 David R. Smith All Rights Reserved.
 *
 */
package com.cryptoregistry.formats;

/**
 * Signals if the values are BigIntegers or in some cases byte arrays
 * 
 * @author Dave
 *
 */
public enum EncodingHint {
	NoEncoding,RawBytes,Base2,Base10,Base16,Base64,Base64url;
}
