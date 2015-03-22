/*
 *  This file is part of Buttermilk(TM) 
 *  Copyright 2013 David R. Smith for cryptoregistry.com
 *
 */
package com.cryptoregistry.handle;

import java.util.UUID;

/**
 * <pre>
 * Must be a valid UUID as described here:
 * 
 * http://docs.oracle.com/javase/6/docs/api/java/util/UUID.html
 * 
 * This is validated by calling UUID.fromString(handle) method in java.util.UUID class. If the method throws
 * an IllegalArgumentEexception, the validation fails. 
 * 
 * </pre>
 * 
 * @author Dave
 *
 */
public class UUIDHandle implements Handle {

	private static final long serialVersionUID = 1L;
	protected UUID uuid;
	
	public UUIDHandle(UUID handle) {
		uuid = handle;
	}
	
	public UUIDHandle() {
		uuid = UUID.randomUUID();
	}
	
	@Override
	public boolean validate() {
		
		return true;
	}

	@Override
	public int length() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public char charAt(int index) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public CharSequence subSequence(int start, int end) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int count() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public String[] handleParts() {
		// TODO Auto-generated method stub
		return null;
	}

	public String toString(){
		return uuid.toString();
	}
}
