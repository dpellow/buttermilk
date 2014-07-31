/*
 *  This file is part of Buttermilk
 *  Copyright 2011-2014 David R. Smith All Rights Reserved.
 *
 */
package com.cryptoregistry.formats;

/**
 * Rather than "public" and "private" we have an alternative in "secured", "unsecured", and "for publication"
 * @author Dave
 *
 */
public enum Mode {
	UNSECURED('U'), SECURED('S'), FOR_PUBLICATION('P');
	
	public final char code;

	private Mode(char code) {
		this.code = code;
	}
	
	
}
