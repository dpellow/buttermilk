/*
 *  This file is part of Buttermilk
 *  Copyright 2011-2014 David R. Smith All Rights Reserved.
 *
 */
package com.cryptoregistry.symmetric.mt;

/**
 * Segments are units of data to process (encrypt, decrypt, etc) along with the result of the processing
 * These are inputs to the multi-threaded AESService and its subclasses
 * 
 * @author Dave
 *
 */
public class Segment {
	
	private byte [] input; // data to operate on
	private byte[] output; // result of the operations
	
	public Segment(byte[] input) {
		super();
		this.input = input;
	}

	public byte[] getInput() {
		return input;
	}

	public void setInput(byte[] input) {
		this.input = input;
	}

	public byte[] getOutput() {
		return output;
	}

	public void setOutput(byte[] output) {
		this.output = output;
	}
	
	/**
	 * package protected - Move the output into the input for encrypt/decrypt processing - used for QA testing only
	 */
	void rotate() {
		input = null;
		input = output;
		output = null;
	}
	
	/**
	 * package protected = used to free the input as soon as feasible
	 */
	void freeInput(){
		input = null;
	}
	
}
