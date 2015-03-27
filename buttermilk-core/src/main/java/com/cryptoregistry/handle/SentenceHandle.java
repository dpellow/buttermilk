/*
 *  This file is part of Buttermilk(TM) 
 *  Copyright 2013 David R. Smith for cryptoregistry.com
 *
 */
package com.cryptoregistry.handle;

/**
 * <pre>
 * Handle made of word groups separated by spaces (like a sentence). 
 * 
 * Rules:
 * 
 * 1) Use dictionary words
 * 2) Only one space between words
 * 3) Up to 127 words
 * 4) UTF-8 characters, any language, not a space
 * 5) Max length per word, 34 characters
 * 6) NO embedded html tags
 * 
 * 
 * </pre>
 * @author Dave
 *
 */
public class SentenceHandle extends CryptoHandle {

	private static final long serialVersionUID = 1L;

	public SentenceHandle(String handle) {
		super(handle);
		separator = ' ';
	}

	@Override
	public boolean validate() {
		if(this.count() ==0) return false;
		if(this.count() ==1) return false;
		if(this.count() > 127) return false;
		for(String part: this.handleParts()){
			if(part.length()>34)return false;
		}
		return true;
	}

}
