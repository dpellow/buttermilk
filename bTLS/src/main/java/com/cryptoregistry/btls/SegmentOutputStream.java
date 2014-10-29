package com.cryptoregistry.btls;

import java.io.FilterOutputStream;
import java.io.OutputStream;
import java.nio.charset.Charset;
import java.util.List;

import com.cryptoregistry.crypto.mt.Segment;

/**
 * Secure and write segments
 * 
 * @author Dave
 *
 */
public class SegmentOutputStream extends FilterOutputStream {

	public SegmentOutputStream(OutputStream out) {
		super(out);
	}
	
	/**
	 * Create secure segments and send across the stream
	 * 
	 * @param input
	 */
	public void submit(String input, Charset charset){
		
	}
	
	public void submit(char [] input){
		
	}
	
	public void submit(byte [] input){
		
	}

}
