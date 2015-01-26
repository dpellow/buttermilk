/*
 *  This file is part of Buttermilk
 *  Copyright 2011-2015 David R. Smith All Rights Reserved.
 *
 */
package com.cryptoregistry.util;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class XORUtil {

	private static final Lock lock = new ReentrantLock();
	
	public static final byte [] xor(byte[]a, byte[]b){
		
		lock.lock();
		try {
			if(a.length != b.length) throw new RuntimeException("inputs must be equal lengths");
		
			byte [] result = new byte[a.length];
			for(int i = 0;i<a.length;i++){
				result[i] = (byte)(0xff & ((int)a[i]) ^ ((int)b[i]));
			}
		
			return result;
			
		}finally{
			lock.unlock();
		}
	}

}
