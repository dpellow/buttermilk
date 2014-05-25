/*
 *  This file is part of Buttermilk(TM) 
 *  Copyright 2013 David R. Smith for cryptoregistry.com
 *
 */
package com.cryptoregistry.util;

public class ArrayUtil {
	
	/**
	 * Given an arbitrary number of byte arrays, concatenate them and return a new array with the result in it
	 * 
	 * @param bytes
	 * @return
	 */
	public static byte [] concatenate(byte[]...bytes){
		
		int length = 0;
		for(byte [] item: bytes){
			length+= item.length;
		}
		byte[] array = new byte[length];
		int current =0;
		for(byte [] item: bytes){
			System.arraycopy(item, 0, array, current, item.length);
			current+=item.length;
		}
		
		return array;
	}

}
