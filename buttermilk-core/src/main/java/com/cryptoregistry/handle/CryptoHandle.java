/*
 *  This file is part of Buttermilk(TM) 
 *  Copyright 2013 David R. Smith for cryptoregistry.com
 *
 */
package com.cryptoregistry.handle;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import com.cryptoregistry.util.*;

/**
 * <pre>
 * 
 * Spaces, dashes, dots are valid as the separator char
 * 
 * Maximum number of words is 6, the minimum is 2
 * 
 * </pre>
 * 
 * @author Dave
 *
 */
public abstract class CryptoHandle implements Handle {
	
	private static final long serialVersionUID = 1L;
	final protected String handle;
	protected char separator;

	/**
	 * Default separator is a space character
	 * 
	 * @param handle
	 */
	protected CryptoHandle(String handle) {
		super();
		this.handle = handle;
		separator = ' ';
	}

	@Override
	public int length() {
		return handle.length();
	}

	@Override
	public char charAt(int index) {
		return handle.charAt(index);
	}

	@Override
	public CharSequence subSequence(int beginIndex, int endIndex) {
		return handle.subSequence(beginIndex, endIndex);
	}

	@Override
	public int count() {
		return handleParts().length;
	}


	public abstract boolean validate() ;

	@Override
	public String[] handleParts() {
		return handle.split(String.valueOf(separator));
	}

	public char getSeparator() {
		return separator;
	}

	public void setSeparator(char separator) {
		this.separator = separator;
	}
	
	public String toString() {
		return handle;
	}
	
	public static Handle [] parseHandles(String str){
		String [] strings = str.split("\\|");
		List<Handle> list = new ArrayList<Handle>();
		for(String handle: strings){
			list.add(parseHandle(handle));
		}
		
		return (Handle[]) list.toArray();
	}
	
	/**
	 * Find the handle type based on how many parts are split. For example, the string "www.aol.com" has three parts
	 * when split with "." but 0 parts when split with " " or "-". 
	 * 
	 * @param str
	 * @return
	 */
	public static Handle parseHandle(String str) {
		
		if(str == null) return null;
		if(str == "") return null;
		
		if(str.equals("UNKNOWN")) return NullHandle.UNKNOWN;
		
		int dot = str.split("\\.").length;
		int hyphen = str.split("\\-").length;
		int space = str.split(" ").length;
		
		Count count = Count.findMax(
				new Count(CountType.DOT, dot),
				new Count(CountType.HYPHEN,hyphen),
				new Count(CountType.SPACE,space));
		
		switch(count.getType()){
			case DOT: {
				return new DomainNameHandle(str);
			}
			case HYPHEN: {
				try {
					UUID uuid = UUID.fromString(str);
					return new UUIDHandle(uuid);
				}catch(Exception x){
					return new HyphenatedHandle(str);
				}
			}
			case SPACE: {
				return new SentenceHandle(str);
			}
		}
		return null;
	}
	
	

}
