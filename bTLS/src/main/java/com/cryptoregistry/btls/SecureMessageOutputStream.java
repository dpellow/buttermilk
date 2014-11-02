/*
 *  This file is part of Buttermilk
 *  Copyright 2011-2014 David R. Smith All Rights Reserved.
 *
 */
package com.cryptoregistry.btls;

import java.io.FilterOutputStream;
import java.io.OutputStream;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

import org.apache.log4j.Logger;

import com.cryptoregistry.crypto.mt.SecureMessage;
import com.cryptoregistry.crypto.mt.SecureMessageService;
import com.cryptoregistry.passwords.SensitiveBytes;
import com.cryptoregistry.proto.frame.SecureMessageOutputFrame;

/**
 * Secure and write messages to the stream. 
 * 
 * @author Dave
 *
 */
public class SecureMessageOutputStream extends FilterOutputStream {
	
	private static final Logger log = Logger.getLogger("com.cryptography.btls.SecureMessageOutputStream");
	
	// symmetric key
	SensitiveBytes key;
	int threshhold; // threshhold size, in bytes, for splitting a message into more than one segment 

	public SecureMessageOutputStream(SensitiveBytes key, OutputStream out) {
		super(out);
		this.key = key;
		this.threshhold = 256*1024; // default = 256k and above will use multiple threads
	}
	
	// assume UTF-8
	public void submit(String input){
		submit(input, StandardCharsets.UTF_8);
	}
	
	public void submit(String input, Charset charset){
		
		byte [] bytes = input.getBytes(charset);
		SecureMessage sm = null;
		if(bytes.length>threshhold){
			// break into multiple segments
			sm = new SecureMessage(input,charset);
			log.trace("using multiple segments: "+sm.count());
		}else{
			//for small stuff, constrain to one segment, which means one thread
			sm = new SecureMessage(1,input,charset);
			log.trace("using one segment: "+sm.count());
		}
		
		SecureMessageService service = new SecureMessageService(key.getData(),sm);
		service.encrypt();
		SecureMessageOutputFrame frame = new SecureMessageOutputFrame(sm);
		frame.writeFrame(out);
	}
	
	public void submit(byte [] input){
		
		SecureMessage sm = null;
		if(input.length>threshhold){
			// break into multiple segments
			sm = new SecureMessage(input);
		}else{
			//constrain to one segment, which means one thread
			sm = new SecureMessage(1,input);
		}
		
		SecureMessageService service = new SecureMessageService(key.getData(),sm);
		service.encrypt();
		SecureMessageOutputFrame frame = new SecureMessageOutputFrame(sm);
		frame.writeFrame(out);
	}

	public int getThreshhold() {
		return threshhold;
	}

	public void setThreshhold(int threshhold) {
		if(threshhold < 0) return;
		this.threshhold = threshhold;
	}
	
	@Override
	public void write(int b){
		throw new UnsupportedOperationException("Not a supported method");
	}
	
	@Override
	public void write(byte [] b){
		submit(b);
	}
	
	@Override
	public void write(byte[]b, int off, int len){
		byte [] holder = new byte[len-off];
		System.arraycopy(b, 0, holder, 0, len-off);
		submit(holder);
	}

}
