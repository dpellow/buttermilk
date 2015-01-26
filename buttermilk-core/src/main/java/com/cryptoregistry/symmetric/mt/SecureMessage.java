/*
 *  This file is part of Buttermilk
 *  Copyright 2011-2015 David R. Smith All Rights Reserved.
 *
 */
package com.cryptoregistry.symmetric.mt;

import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * The concept is to break up a larger raw message into roughly equal pieces and encrypt these pieces separately 
 * on different threads. This allows us to take better advantage of contemporary multi-core processors.
 * 
 * Effective max message size limit is about 100Mb. The limitation is basically one of memory, not cores.
 * A message smaller than about 5Mb can be processed faster with a single thread. 
 * 
 * @author Dave
 * 
 * @see SecureMessageService
 *
 */
public class SecureMessage {
	
	private final int CORE_COUNT = Runtime.getRuntime().availableProcessors(); // not static so we can adjust cores dynamically, e.g. when using VMWare or similar platform 

	private static final SecureRandom rand = new SecureRandom();
	
	final SecureMessageHeader header;
	final List<Segment> segments;
	
	// assume UTF-8
	public SecureMessage(String str) {
		segments = new ArrayList<Segment>();
		createSegments(CORE_COUNT,str.getBytes(StandardCharsets.UTF_8));
		byte [] ivBytes = new byte[16];
		rand.nextBytes(ivBytes);
		header = new SecureMessageHeader(ivBytes);
	}
	
	public SecureMessage(String str, Charset charset) {
		segments = new ArrayList<Segment>();
		createSegments(CORE_COUNT,str.getBytes(charset));
		byte [] ivBytes = new byte[16];
		rand.nextBytes(ivBytes);
		header = new SecureMessageHeader(ivBytes);
	}
	
	/**
	 * This constructor exists mainly to allow for single-threaded processing for small messages. Just
	 * set the threads value to 1. 
	 * 
	 * @param threads
	 * @param str
	 * @param charset
	 */
	public SecureMessage(int threads, String str, Charset charset) {
		if(threads < 1 || threads > 256) throw new RuntimeException("Huh?");
		segments = new ArrayList<Segment>();
		createSegments(threads,str.getBytes(charset));
		byte [] ivBytes = new byte[16];
		rand.nextBytes(ivBytes);
		header = new SecureMessageHeader(ivBytes);
	}
	
	public SecureMessage(char [] input) {
		segments = new ArrayList<Segment>();
		CharBuffer charBuffer = CharBuffer.wrap(input);
		ByteBuffer byteBuffer = Charset.forName("UTF-8").encode(charBuffer);
		byte[] bytes = Arrays.copyOfRange(byteBuffer.array(), byteBuffer.position(), byteBuffer.limit());
		createSegments(CORE_COUNT,bytes);
		byte [] ivBytes = new byte[16];
		rand.nextBytes(ivBytes);
		header = new SecureMessageHeader(ivBytes);
	}
	
	public SecureMessage(byte [] input) {
		segments = new ArrayList<Segment>();
		createSegments(CORE_COUNT,input);
		byte [] ivBytes = new byte[16];
		rand.nextBytes(ivBytes);
		header = new SecureMessageHeader(ivBytes);
	}
	
	public SecureMessage(int threads, byte [] input) {
		if(threads < 1 || threads > 256) throw new RuntimeException("Huh?");
		segments = new ArrayList<Segment>();
		createSegments(threads,input);
		byte [] ivBytes = new byte[16];
		rand.nextBytes(ivBytes);
		header = new SecureMessageHeader(ivBytes);
	}
	
	public SecureMessage(SecureMessageHeader header, List<Segment> segments) {
		this.segments = segments;
		this.header = header;
	}
	
	public int count() {
		return segments.size();
	}
	
	/**
	 * Create a list of Segments from a byte array
	 * 
	 * @param count
	 * @param totalBytes
	 * @return
	 */
	protected void createSegments(int count, byte[]totalBytes){
		int bufSize = totalBytes.length/count;
		int remainder = totalBytes.length - (bufSize *count);
		
		for(int i = 0;i<count;i++){
			byte [] b = new byte[bufSize];
			System.arraycopy(totalBytes, i*bufSize, b, 0, bufSize);
			segments.add(new Segment(b));
		}
		
		// do the last bits if required
		if(remainder > 0){
			byte [] remainderBytes = new byte[remainder];
			System.arraycopy(totalBytes, totalBytes.length-remainder, remainderBytes, 0, remainderBytes.length);
			segments.add(new Segment(remainderBytes));
		}
	}

	public List<Segment> getSegments() {
		return segments;
	}

	public SecureMessageHeader getHeader() {
		return header;
	}
	
	public void rotate() {
		for(Segment s: segments){
			s.rotate();
		}
	}
	
	public int totalSizeOutput() {
		int size = 0;
		for(Segment s: segments){
			size+=s.getOutput().length;
		}
		return size;
	}
	
	public int totalSizeInput() {
		int size = 0;
		for(Segment s: segments){
			size+=s.getInput().length;
		}
		return size;
	}
	

	/**
	 * Create a UTF-8 String from the result bytes
	 * @return
	 */
	public String stringResult() {
		StringBuilder builder = new StringBuilder();
		for(Segment s: this.segments){
			builder.append(new String(s.getOutput(),StandardCharsets.UTF_8));
		}
		return builder.toString();
	}
	
	/**
	 * Build the result bytes parts into a complete byte array
	 * @return
	 */
	public byte [] byteResult() {
	
		byte [] sz = new byte[this.totalSizeOutput()];
		int c = 0;
		for(Segment s: this.segments){
			System.arraycopy(s.getOutput(), 0, sz, c, s.getOutput().length);
			c+=s.getOutput().length;
		}
		
		return sz;
		
	}
	
}
