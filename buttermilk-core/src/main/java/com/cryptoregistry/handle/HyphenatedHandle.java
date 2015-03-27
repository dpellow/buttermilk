/*
 *  This file is part of Buttermilk(TM) 
 *  Copyright 2013 David R. Smith for cryptoregistry.com
 *
 */
package com.cryptoregistry.handle;

/**
 * <pre>
 * 
 * Hyphen separates word groups. This handle allows for the scenario where the user wants 
 * spaces in their word groups. 
 * 
 * Rules:
 * 
 * 1) From 2 to 127 "words"
 * 2) Can contain any UTF-8 character in the "word", but not a hyphen. 
 * 3) Max length of each "word" is 34 bytes
 * 
 * </pre>
 * @author Dave
 *
 */
public class HyphenatedHandle extends CryptoHandle {

	private static final long serialVersionUID = 1L;

	public HyphenatedHandle(String handle) {
		super(handle);
		separator = '-';
	}
	
	@Override
	public String[] handleParts() {
		return handle.split("\\"+separator);
	}

	@Override
	public boolean validate() {
		return false;
		/*
		int count = this.count();
		if(count < 2 || count > 127) return false;
		for(String part: this.handleParts()){
			if(part.length()>34)return false;
		}
		return true;
		*/
	}

}
