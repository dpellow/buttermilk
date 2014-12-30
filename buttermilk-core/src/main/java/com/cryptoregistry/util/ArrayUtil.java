/*
 *  This file is part of Buttermilk(TM) 
 *  Copyright 2013 David R. Smith for cryptoregistry.com
 *
 */
package com.cryptoregistry.util;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.IntBuffer;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

public class ArrayUtil {
	
	private static final Lock lock = new ReentrantLock();
	
	/**
	 * Given an arbitrary number of byte arrays, concatenate them and return a new array 
	 * with a copy of all the bytes in it
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
	
	public static byte [] compressIntArray(int [] array) throws IOException{
		ByteBuffer byteBuffer = ByteBuffer.allocate(array.length * 4);        
        IntBuffer intBuffer = byteBuffer.asIntBuffer();
        intBuffer.put(array);
        byte [] uncompressed = byteBuffer.array();
        ByteArrayOutputStream bout = new ByteArrayOutputStream();
        GZIPOutputStream out = new GZIPOutputStream(bout);
        out.write(uncompressed, 0, uncompressed.length);
        out.finish();
        return bout.toByteArray();
	}
	
	public static int [] uncompressIntArray(byte [] compressed) {
		
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		GZIPInputStream in=null;
		try {
			 ByteArrayInputStream bin = new ByteArrayInputStream(compressed);
			 in = new GZIPInputStream(bin);
			 byte[] buffer = new byte[1024];
			 int count=0;
			 while((count = in.read(buffer,0,buffer.length))!=-1){
				 out.write(buffer,0,count);
			 }
			
		}catch(IOException x){
			 x.printStackTrace();
		 }finally{
			 try {
				out.close();
				in.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		 }
		IntBuffer intBuf = ByteBuffer.wrap(out.toByteArray()).asIntBuffer();
	    int [] array = new int[intBuf.remaining()];
	    intBuf.get(array);
	    return array;
	}
	
	public static ArmoredCompressedString wrapAndCompressIntArray(int [] array){
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
	
	public static ArmoredString wrapIntArray(int [] array){
		ByteBuffer byteBuffer = ByteBuffer.allocate(array.length * 4);        
        IntBuffer intBuffer = byteBuffer.asIntBuffer();
        intBuffer.put(array);
        return new ArmoredString(byteBuffer.array());
	}
	
	public static int [] unwrapIntArray(ArmoredString in){
		byte [] encoded = in.decodeToBytes();
		IntBuffer intBuf = ByteBuffer.wrap(encoded).asIntBuffer();
	    int [] array = new int[intBuf.remaining()];
	    intBuf.get(array);
	    return array;
	}

}
