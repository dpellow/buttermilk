/*
 *  This file is part of Buttermilk(TM) 
 *  Copyright 2013-2014 David R. Smith for cryptoregistry.com
 *
 */
package com.cryptoregistry.util.entropy;

import java.text.DecimalFormat;
import java.util.ArrayList;

/**
 * Implementation of bientropy algorithm as described in
 * 
 * http://arxiv.org/ftp/arxiv/papers/1305/1305.0954.pdf
 * 
 * This class is really useful only for one byte inputs which is probably not
 * what brought you looking for this code. See TresBiEntropy class for 
 * arbitrary length inputs like can be used with a password
 * 
 * @author Dave
 * @TresBiEntropy
 */
public class BiEntropy {

	private byte input;
	private ArrayList<Character> binaryExpansion;
	
	final static Character ZERO = new Character('0');
	final static Character ONE = new Character('1');
	final DecimalFormat format = new DecimalFormat("#######0.00");
	
	double U, T; // intermediate values
	
	public BiEntropy(byte input) {
		this.input = input;
		binaryExpansion = new ArrayList<Character>();
	}
	
	/**
	 * Convert the byte from a 2's complement (signed byte) value into an unsigned integer value
	 */
	private void collect(){
		int b = ((int) input) & 0xFF; 
		push(b);
	}
	
	/**
	 * Convert byte into a char array of ones and zeroes characters, push 
	 * on to the binaryExpansion stack
	 * 
	 * @param b
	 */
	private void push(int b){
		char [] word = bitsAsString(b).toCharArray();
		for(char ch : word){
			binaryExpansion.add(ch);
		}
	}
	
	/**
	 * Compute bientropy for the primary binary expansion and then recurse to do
	 * the derivatives.
	 * 
	 * @param list
	 */
	private void compute(ArrayList<Character> list) {
		
		double k = 0;
		double l = list.size();
		for(int i = 0; i<list.size();i++){
			if(list.get(i).equals(ONE)){
				k++;
			}
		}
		
		double p = k/l;
		double p1 = 1-p;
		double p2 = p == 0 ? 0 : (-p * (Math.log(p)/Math.log(2)));
		double q = p1 == 0 ? 0 : (-p1 * (Math.log(p1)/Math.log(2)));
		double bien = p2+q;
	
		double r = binaryExpansion.size() - (list.size() - 1);
		double r1 = Math.pow(2, r-1);
		double u = bien*r1;
		
		T+=r1;
		U+=u;
		
		System.err.println("binaryExpansion="+list+", BiEn="+format.format(bien)+", k="+r+", (r-1)^2="+r1+", bien*(r-1)^2="+format.format(u));
			
		// don't do a list of 1 item or less - escape recurse
		if( list.size() == 2) return;
		
		// build the derivative binary expansion
		ArrayList<Character> derivative = new ArrayList<Character>();
		for(int i = 0; i<list.size()-1;i++){
			Character c0 = list.get(i);
			Character c1 = list.get(i+1);
			if(c0.equals(c1)) derivative.add(ZERO);
			else derivative.add(ONE);
		}
		
		compute(derivative);
		
	}
	
	
	
	public Result calc() {
		
		// initial state
		U=T=0;
		binaryExpansion.clear();
		
		collect();
		compute(binaryExpansion);
		double res = U/T;
		return new Result(res, res*1*8);
	}
	

	private String bitsAsString(int b) {
		String s = Integer.toBinaryString(b);
		if(s.length() > 8) throw new RuntimeException("Expecting to work with 8 bits or less");
		if(s.length() == 8) return s;
		else if(s.length() == 7) return "0"+s;
		else if(s.length() == 6) return "00"+s;
		else if(s.length() == 5) return "000"+s;
		else if(s.length() == 4) return "0000"+s;
		else if(s.length() == 3) return "00000"+s;
		else if(s.length() == 2) return "000000"+s;
		else if(s.length() == 1) return "0000000"+s;
		else if(s.length() == 0) return "00000000";
		else return "";
	}
	
	public static void main(String [] args){
		BiEntropy bi = new BiEntropy((byte) 'x');
		Result result = bi.calc();
		System.err.println(result);
	}
	
	class Result {
		
		public double biEntropy;
		public double bitsOfEntropy;
		
		public Result(double biEntropy, double bitsOfEntropy) {
			super();
			this.biEntropy = biEntropy;
			this.bitsOfEntropy = bitsOfEntropy;
		}

		@Override
		public String toString() {
			return "Result [biEntropy=" + format.format(biEntropy) + ", bitsOfEntropy="
					+ Math.round(bitsOfEntropy) + "]";
		}

		@Override
		public int hashCode() {
			final int prime = 31;
			int result = 1;
			result = prime * result + getOuterType().hashCode();
			long temp;
			temp = Double.doubleToLongBits(biEntropy);
			result = prime * result + (int) (temp ^ (temp >>> 32));
			temp = Double.doubleToLongBits(bitsOfEntropy);
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
			if (!getOuterType().equals(other.getOuterType()))
				return false;
			if (Double.doubleToLongBits(biEntropy) != Double
					.doubleToLongBits(other.biEntropy))
				return false;
			if (Double.doubleToLongBits(bitsOfEntropy) != Double
					.doubleToLongBits(other.bitsOfEntropy))
				return false;
			return true;
		}

		private BiEntropy getOuterType() {
			return BiEntropy.this;
		}
	}

}
