package com.cryptoregistry.util;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class Microbenchmark {

	private String name;
	private Deque<Long> times;
	private Lock lock = new ReentrantLock();
	private int maxDepth = 100;

	long start;

	public Microbenchmark(String name) {
		times = new ArrayDeque<Long>();
		this.name = name;
	}

	public Microbenchmark start() {
		lock.lock();
		try {
			start = System.nanoTime();
			return this;
		} finally {
			lock.unlock();
		}
	}

	public Microbenchmark stop() {
		lock.lock();
		try {
			long stop = System.nanoTime();
			times.add(stop - start);
			if(times.size()>=maxDepth){
				times.removeFirst(); // remove oldest in order of being added
			}
			start = 0;
			return this;
		} finally {
			lock.unlock();
		}
	}
	
	public Microbenchmark clear() {
		lock.lock();
		try {
			times.clear();
			start = 0;
			return this;
		} finally {
			lock.unlock();
		}
	}

	public long min() {
		lock.lock();
		try {
			long lowest = 0;
			boolean first = true;
			for (Long t : times) {
				if (first) {
					lowest = t;
					first = false;
				}
				if (t < lowest)
					lowest = t;
			}
			return TimeUnit.MILLISECONDS.convert(lowest, TimeUnit.NANOSECONDS);
		} finally {
			lock.unlock();
		}
	}

	public long max() {
		lock.lock();
		try {
			long highest = 0;
			boolean first = true;
			for (Long t : times) {
				if (first) {
					highest = t;
					first = false;
				}
				if (t > highest)
					highest = t;
			}
			return TimeUnit.MILLISECONDS.convert(highest, TimeUnit.NANOSECONDS);
		} finally {
			lock.unlock();
		}
	}

	public double avg() {
		lock.lock();
		try {
			Long sum = 0L;
			if (!times.isEmpty()) {
				for (Long t : times) {
					sum += t;
				}
				return TimeUnit.MILLISECONDS.convert((long)(sum.doubleValue() / times.size()), TimeUnit.NANOSECONDS);
			}else{
				return 0;
			}
			
		} finally {
			lock.unlock();
		}
	}

	public String toString() {
		String format = "----------------------\n%s\nMin: %dms\nMax: %dms\nAvg: %.2fms\n----------------------";
		return String.format(format, name, min(), max(), avg());
	}
	
	public String tabular() {
		String format = "%s\tMin: %dms\tMax: %dms\tAvg: %.2fms";
		return String.format(format, name, min(), max(), avg());
	}

	public int getMaxDepth() {
		return maxDepth;
	}

	public void setMaxDepth(int maxDepth) {
		if(maxDepth <=0)return;
		this.maxDepth = maxDepth;
	}

	public Deque<Long> getTimes() {
		return times;
	}

}
