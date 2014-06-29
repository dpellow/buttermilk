/*
 *  This file is part of Buttermilk(TM) 
 *  Copyright 2013 David R. Smith for cryptoregistry.com
 *
 */
package com.cryptoregistry.util;

import java.nio.ByteBuffer;
import java.nio.IntBuffer;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class ArrayUtil {
	
	private static final Lock lock = new ReentrantLock();
	
	/**
	 * Given an arbitrary number of byte arrays, concatenate them and return a new array with the result in it
	 * 
	 * @param bytes
	 * @return
	 */
	public static byte [] concatenate(byte[]...bytes){
		lock.lock();
		try {
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
		}finally {
			lock.unlock();
		}
	}
	
	public static ArmoredCompressedString wrapIntArray(int [] array){
		ByteBuffer byteBuffer = ByteBuffer.allocate(array.length * 4);        
        IntBuffer intBuffer = byteBuffer.asIntBuffer();
        intBuffer.put(array);
        return new ArmoredCompressedString(byteBuffer.array());
	}
	
	public static int [] unwrapIntArray(ArmoredCompressedString in){
		byte [] encoded = in.decodeToBytes();
		IntBuffer intBuf = ByteBuffer.wrap(encoded).asIntBuffer();
	    int [] array = new int[intBuf.remaining()];
	    intBuf.get(array);
	    return array;
	}

}
