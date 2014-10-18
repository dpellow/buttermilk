/*
 *  This file is part of Buttermilk
 *  Copyright 2011-2014 David R. Smith All Rights Reserved.
 *
 */
package com.cryptoregistry.crypto.mt;

import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

public class Segment {
	
	public final int position; // index, zero based
	public final byte [] input;
	byte[] output;
	
	public Segment(int position, byte[] input) {
		super();
		this.position = position;
		this.input = input;
	}

	public byte[] getOutput() {
		return output;
	}
	
	public static List<Segment> createSegments(String str){
		int cores = Runtime.getRuntime().availableProcessors();
		return createSegments(cores,str);
	}
	
	public static List<Segment> createSegments( byte[]totalBytes){
		int cores = Runtime.getRuntime().availableProcessors();
		return createSegments(cores, totalBytes);
	}
	
	public static List<Segment> createSegments(int count, String str){
		byte [] totalBytes = str.getBytes(Charset.forName("UTF-8"));
		return createSegments(count, totalBytes);
	}
	
	public static List<Segment> createSegments(int count, byte[]totalBytes){
		int bufSize = totalBytes.length/count;
		int remainder = totalBytes.length - (bufSize *count);
		
		ArrayList<Segment> list = new ArrayList<Segment>();
		for(int i = 0;i<count;i++){
			byte [] b = new byte[bufSize];
			System.arraycopy(totalBytes, i*bufSize, b, 0, bufSize);
			list.add(new Segment(i,b));
		}
		if(remainder > 0){
			byte [] remainderBytes = new byte[remainder];
			System.arraycopy(totalBytes, totalBytes.length-remainder, remainderBytes, 0, remainderBytes.length);
			list.add(new Segment(count+1,remainderBytes));
		}
		return list;
	}
	
}
