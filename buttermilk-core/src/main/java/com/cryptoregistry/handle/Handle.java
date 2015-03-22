/*
 *  This file is part of Buttermilk(TM) 
 *  Copyright 2013 David R. Smith for cryptoregistry.com
 *
 */
package com.cryptoregistry.handle;

import java.io.Serializable;

/** <pre>
 * 
 * A Handle is an identifier for key materials
 *
 * </pre>
 * @author Dave
 *
 */
public interface Handle extends CharSequence, Serializable {

	/**
	 * Number of 'words' 
	 * 
	 * @return
	 */
	public int count();
	
	/**
	 * Validate the contents really qualifies as a handle (semantically)
	 * 
	 * @return
	 */
	public boolean validate();
	
	public String [] handleParts();
	
	
	
}
