/*
 *  This file is part of Buttermilk(TM) 
 *  Copyright 2013 David R. Smith for cryptoregistry.com
 *
 */
package com.cryptoregistry.handle;

import java.nio.charset.Charset;
import java.nio.charset.CharsetEncoder;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.cryptoregistry.util.DomainTLDChecker;

/**
 * <pre>
 * Handle for domain names
 * 
 * Rules:
 * 
 * 1) Characters must be a subset of ascii and contain only letters, numbers, and hyphen (dash).
 * 2) Max length is 256 chars, some domain TLDs appear to accept only shorter names than the spec.
 * 3) max count of "words" is 127
 * 
 * </pre>
 * 
 * @author Dave
 *
 */
public class DomainNameHandle extends CryptoHandle {
	
	private static final long serialVersionUID = 1L;
	protected static final Pattern LDH = Pattern.compile("[A-Za-z0-9-]+"); 
	protected static final DomainTLDChecker checker = new DomainTLDChecker();

	public DomainNameHandle(String handle) {
		super(handle);
		separator = '.';
	}
	
	@Override
	public String[] handleParts() {
		return handle.split("\\"+separator);
	}

	/**
	 * Try to figure if it looks like a domain name from the syntactic point of view. 
	 * This method does not actually check for DNS resolution, we want to support 
	 * private/internal domains. 
	 * 
	 */
	@Override
	public boolean validate() {
		
		if(handle.length() >256) return false;
		if(!isPureAscii(handle)) return false;
		String [] parts = this.handleParts();
		if(parts.length<2) return false;
		if(parts.length>127) return false;
		for(String part : parts){
			Matcher m = LDH.matcher(part);
			if(!m.matches()) return false;
		}
		
		String last = parts[parts.length-1];
		if(!checker.isPresent(last)) return false;
		
		return true;
	}
	
	// taken from http://stackoverflow.com/questions/3585053/in-java-is-it-possible-to-check-if-a-string-is-only-ascii
	
		private final static CharsetEncoder asciiEncoder = Charset.forName("US-ASCII").newEncoder(); // or "ISO-8859-1" for ISO Latin 1

		public static boolean isPureAscii(String v) {
			 return asciiEncoder.canEncode(v);
		}

}
