package com.cryptoregistry.util;

import java.util.Iterator;
import java.util.Map;
import java.util.TreeMap;

/**
 * Simple micro-benchmarking
 * 
 * @author Dave
 *
 */
public class StopWatch {
	
	private boolean on;
	
	public static final StopWatch INSTANCE = new StopWatch();
	
	private Map<String, Microbenchmark> map;
	
	private StopWatch() {
		map = new TreeMap<String,Microbenchmark>();
	}
	
	public void on() {
		on=true;
	}
	
	public void off() {
		on=false;
	}
	
	public Microbenchmark add(String name){
		if(!on) return null;
		map.put(name, new Microbenchmark(name));
		return map.get(name);
	}
	
	public Microbenchmark find(String name){
		return map.get(name);
	}
	
	public Microbenchmark start(String name){
		if(!on) return null;
		if(!map.containsKey(name)){
			this.add(name);
		}
		map.get(name).start();
		return map.get(name);
	}
	
	public Microbenchmark stop(String name){
		if(!on) return null;
		if(!map.containsKey(name)){
			this.add(name);
		}
		map.get(name).stop();
		return map.get(name);
	}
	
	public void print(String name){
		if(!on) return;
		if(!map.containsKey(name)) return;
		System.out.println(map.get(name).toString());
	}
	
	public void clearAll() {
		if(!on) return;
		Iterator<String> iter = map.keySet().iterator();
		while(iter.hasNext()){
			String name = iter.next();
			map.get(name).clear();
		}
	}
	
	public void printAll() {
		if(!on) return;
		Iterator<String> iter = map.keySet().iterator();
		StringBuffer buf = new StringBuffer();
		while(iter.hasNext()){
			String name = iter.next();
			String s = map.get(name).tabular();
			buf.append(s);
			buf.append("\n");
		}
		
		System.out.println(buf.toString());
	}
}
