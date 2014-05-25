/*
 *  This file is part of Buttermilk(TM) 
 *  Copyright 2013-2014 David R. Smith for cryptoregistry.com
 *
 */
package com.cryptoregistry.util.entropy;

import java.lang.Math;
import java.util.Map;
import java.util.HashMap;

/**
 * Get entropy by the Shannon method. Reworked from: http://rosettacode.org/wiki/Entropy#Java
 * 
 * @author Dave
 * 
 */
public final class ShannonEntropy {
	
	private static double getShannonEntropy(char [] s) {
		int n = 0;
		Map<Character, Integer> map = new HashMap<>();

		for (int c_ = 0; c_ < s.length; ++c_) {
			char cx = s[c_];
			if (map.containsKey(cx)) {
				map.put(cx, map.get(cx) + 1);
			} else {
				map.put(cx, 1);
			}
			++n;
		}

		double e = 0.0;
		for (Map.Entry<Character, Integer> entry : map.entrySet()) {
			double p = (double) entry.getValue() / n;
			e += p * log2(p);
		}
		return -e;
	}

	@SuppressWarnings("boxing")
	private static double getShannonEntropy(String s) {
		int n = 0;
		Map<Character, Integer> map = new HashMap<>();

		for (int c_ = 0; c_ < s.length(); ++c_) {
			char cx = s.charAt(c_);
			if (map.containsKey(cx)) {
				map.put(cx, map.get(cx) + 1);
			} else {
				map.put(cx, 1);
			}
			++n;
		}

		double e = 0.0;
		for (Map.Entry<Character, Integer> entry : map.entrySet()) {
			double p = (double) entry.getValue() / n;
			e += p * log2(p);
		}
		return -e;
	}

	private static double log2(double a) {
		return Math.log(a) / Math.log(2);
	}
	
	public static int bitsOfEntropy(char [] input) {
		if (input == null || input.length == 0)
			return 0;
		return (int) getShannonEntropy(input) * input.length;
	}

	public static int bitsOfEntropy(String input) {
		return bitsOfEntropy(input.toCharArray());
	}
	
	public static Result shannonEntropy(String input){
		double ent = getShannonEntropy(input);
		return new Result(ent,(int)(ent*input.length()));
	}
	
	static class Result {
		
		public double shannonEntropy;
		public int bitsOfEntropy;
		
		public Result(double shannonEntropy, int bitsOfEntropy) {
			super();
			this.shannonEntropy = shannonEntropy;
			this.bitsOfEntropy = bitsOfEntropy;
		}

		@Override
		public String toString() {
			return "Result [shannonEntropy=" + shannonEntropy
					+ ", bitsOfEntropy=" + bitsOfEntropy + "]";
		}

		@Override
		public int hashCode() {
			final int prime = 31;
			int result = 1;
			result = prime * result + bitsOfEntropy;
			long temp;
			temp = Double.doubleToLongBits(shannonEntropy);
			result = prime * result + (int) (temp ^ (temp >>> 32));
			return result;
		}

		@Override
		public boolean equals(Object obj) {
			if (this == obj)
				return true;
			if (obj == null)
				return false;
			if (getClass() != obj.getClass())
				return false;
			Result other = (Result) obj;
			if (bitsOfEntropy != other.bitsOfEntropy)
				return false;
			if (Double.doubleToLongBits(shannonEntropy) != Double
					.doubleToLongBits(other.shannonEntropy))
				return false;
			return true;
		}
	}
}
