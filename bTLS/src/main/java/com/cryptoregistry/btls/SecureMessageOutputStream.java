/*
 *  This file is part of Buttermilk
 *  Copyright 2011-2014 David R. Smith All Rights Reserved.
 *
 */
package com.cryptoregistry.btls;

import java.io.FilterOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

import com.cryptoregistry.crypto.mt.SecureMessage;
import com.cryptoregistry.crypto.mt.SecureMessageService;
import com.cryptoregistry.passwords.SensitiveBytes;
import com.cryptoregistry.proto.builder.SecureMessageProtoBuilder;
import com.cryptoregistry.protos.Buttermilk.SecureMessageProto;

/**
 * Secure and write messages to the stream. 
 * 
 * @author Dave
 *
 */
public class SecureMessageOutputStream extends FilterOutputStream {
	
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
		}else{
			//constrain to one segment, which means one thread
			sm = new SecureMessage(1,input,charset);
		}
		
		SecureMessageService service = new SecureMessageService(key.getData(),sm);
		service.encrypt();
		SecureMessageProtoBuilder builder = new SecureMessageProtoBuilder(sm);
		SecureMessageProto proto = builder.build();
		try {
			proto.writeTo(out);
			out.flush();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
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
		SecureMessageProtoBuilder builder = new SecureMessageProtoBuilder(sm);
		SecureMessageProto proto = builder.build();
		try {
			proto.writeTo(out);
			out.flush();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public int getThreshhold() {
		return threshhold;
	}

	public void setThreshhold(int threshhold) {
		if(threshhold < 0) return;
		this.threshhold = threshhold;
	}

}
