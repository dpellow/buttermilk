/*
 *  This file is part of Buttermilk
 *  Copyright 2011-2014 David R. Smith All Rights Reserved.
 *
 */
package com.cryptoregistry.crypto.mt;

import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

/**
 * Segments are units of data to process (encrypt, decrypt, etc). These are inputs to the multi-threaded AESService
 * 
 * @author Dave
 *
 */
public class Segment {
	
	public final int position; // index within the set of segments; for example this might be segment 0
	public final int count; // total number of segments to expect; for example, 3. So this could be 1 of 3.
	public final byte [] input; // data to operate on
	byte[] output; // result of the encryption (or other) operation
	
	public Segment(int position, int count, byte[] input) {
		super();
		this.position = position;
		this.count = count;
		this.input = input;
	}

	public byte[] getOutput() {
		return output;
	}
	
	/**
	 * Use the number of available cores to determine the number of segments we cut the string into.
	 * 
	 * @param str
	 * @return
	 */
	public static List<Segment> createSegments(String str){
		int cores = Runtime.getRuntime().availableProcessors();
		return createSegments(cores,str);
	}
	
	/**
	 * Use the number of available cores to determine the number of segments we cut the byte array into.
	 * 
	 * @param str
	 * @return
	 */
	public static List<Segment> createSegments( byte[]totalBytes){
		int cores = Runtime.getRuntime().availableProcessors();
		return createSegments(cores, totalBytes);
	}
	
	public static List<Segment> createSegments(int count, String str){
		return createSegments(count, str, Charset.forName("UTF-8"));
	}
	
	public static List<Segment> createSegments(int count, String str, Charset charset){
		byte [] totalBytes = str.getBytes(charset);
		return createSegments(count, totalBytes);
	}
	
	/**
	 * Assume UTF-8
	 * 
	 * @param list
	 * @return
	 */
	public static String combineToString(List<Segment> list){
		StringBuilder builder = new StringBuilder();
		for(Segment s: list){
			builder.append(new String(s.output,Charset.forName("UTF-8")));
		}
		return builder.toString();
	}
	
	/**
	 * Create a list of Segments from a byte array
	 * 
	 * @param count
	 * @param totalBytes
	 * @return
	 */
	public static List<Segment> createSegments(int count, byte[]totalBytes){
		int bufSize = totalBytes.length/count;
		int remainder = totalBytes.length - (bufSize *count);
		int numberOfSegments = (remainder == 0) ? count : count+1;
		
		ArrayList<Segment> list = new ArrayList<Segment>();
		for(int i = 0;i<count;i++){
			byte [] b = new byte[bufSize];
			System.arraycopy(totalBytes, i*bufSize, b, 0, bufSize);
			list.add(new Segment(i,numberOfSegments,b));
		}
		if(remainder > 0){
			byte [] remainderBytes = new byte[remainder];
			System.arraycopy(totalBytes, totalBytes.length-remainder, remainderBytes, 0, remainderBytes.length);
			list.add(new Segment(count+1,numberOfSegments,remainderBytes));
		}
		return list;
	}
	
}
