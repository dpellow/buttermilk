/*
 *  This file is part of Buttermilk
 *  Copyright 2011-2014 David R. Smith All Rights Reserved.
 *
 */
package com.cryptoregistry.symmetric.mt;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

/**
 * Process a list of Segment objects using an Executor and a set of threads
 * 
 * @author Dave
 *
 */
public class AESService {

	private final ExecutorService pool;
	private final byte [] key, iv;
	 
	// ask the JVM about how many cores are available
	public AESService(byte [] key, byte[] iv) {
		int cores = Runtime.getRuntime().availableProcessors();
	//	System.err.println("Cores: "+cores);
		pool = Executors.newFixedThreadPool(cores);
		this.key = key;
		this.iv = iv;
	}
	 
	// manually set threads count. Using a value close to the number of cores available in production 
	// is a good idea
	
	public AESService(int poolSize, byte [] key, byte[] iv) {
		pool = Executors.newFixedThreadPool(poolSize);
		this.key = key;
		this.iv = iv;
	}

	public List<Future<Segment>> runEncryptTasks(List<Segment> segments){
		ArrayList<Encryptor> list = new ArrayList<Encryptor>();
		for(Segment s: segments){
			Encryptor enc = new Encryptor(key,iv,s);
			list.add(enc);
		}
		try {
			return pool.invokeAll(list);
		} catch (InterruptedException e) {
			throw new RuntimeException(e);
		}
	}
	
	public List<Future<Segment>> runDecryptTasks(List<Segment> segments){
		ArrayList<Decryptor> list = new ArrayList<Decryptor>();
		for(Segment s: segments){
			Decryptor enc = new Decryptor(key,iv,s);
			list.add(enc);
		}
		try {
			return pool.invokeAll(list);
		} catch (InterruptedException e) {
			throw new RuntimeException(e);
		}
	}

}

