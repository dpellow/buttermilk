package com.cryptoregistry.util;

import java.util.HashMap;
import java.util.Map;

/**
 * Simple micro-benchmarking
 * 
 * @author Dave
 *
 */
public class StopWatch {

	public static final StopWatch INSTANCE = new StopWatch();
	
	private Map<String, Microbenchmark> map;
	
	private StopWatch() {
		map = new HashMap<String,Microbenchmark>();
	}
	
	public Microbenchmark add(String name){
		map.put(name, new Microbenchmark(name));
		return map.get(name);
	}
	
	public Microbenchmark start(String name){
		map.get(name).start();
		return map.get(name);
	}
	
	public Microbenchmark stop(String name){
		map.get(name).stop();
		return map.get(name);
	}
	
	public void print(String name){
		if(!map.containsKey(name)) return;
		System.out.println(map.get(name).toString());
	}
}
