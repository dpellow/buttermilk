/*
 *  This file is part of Buttermilk
 *  Copyright 2011-2014 David R. Smith All Rights Reserved.
 *
 */
package com.cryptoregistry.util.entropy;

import java.io.IOException;
import java.io.StringWriter;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Map;

import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

/**
 * Implementation of tres bientropy algorithm described in
 * 
 * http://arxiv.org/ftp/arxiv/papers/1305/1305.0954.pdf
 * 
 * Suitable for arbitrary length byte arrays such as keys or passwords
 * 
 * @author Dave
 *
 */
public class TresBiEntropy {

	private byte [] input;
	private ArrayList<Character> binaryExpansion;
	
	final Character ZERO = new Character('0');
	final Character ONE = new Character('1');
	
	double U, T; // intermediate values
	
	public TresBiEntropy(byte[] input) {
		this.input = input;
		binaryExpansion = new ArrayList<Character>();
	}
	
	/**
	 * Convert each byte from a 2's complement value into an unsigned integer value
	 */
	private int collect(){
		for(int i = 0;i<input.length;i++){
			int b = ((int) input[i]) & 0xFF; 
			push(b);
		}
		
		return input.length;
	}
	
	/**
	 * Convert each byte into a char array of ones and zeroes characters, push 
	 * onto the binaryExpansion stack
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
	 * Compute bientropy for the primary binary expansion and then recurse to do the derivatives.
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
	
		double r = binaryExpansion.size() - (list.size());
		double r1 = Math.log(r+2)/Math.log(2);
		double u = bien*(Math.log(r+2)/Math.log(2));
		
		T+=r1;
		U+=u;
		
	//	System.err.println("bien="+format.format(bien)+", k="+r+", log(k+2)="+r1+", bien*log(k+2)="+format.format(u));
		
		// don't do a list of 1 item - escape
		if( list.size() == 2) return;
		
		ArrayList<Character> derivative = new ArrayList<Character>();
		for(int i = 0; i<list.size()-1;i++){
			Character c0 = list.get(i);
			Character c1 = list.get(i+1);
			if(c0.equals(c1)) derivative.add(ZERO);
			else derivative.add(ONE);
		}
		
		try {
			compute(derivative);
		}catch(StackOverflowError x){
			//can happen if input is empty
			x.printStackTrace();
		}
		
	}
	
	public Result calc() {
		
		// initial state
		U=T=0;
		binaryExpansion.clear();
		
		int length = collect();
		compute(binaryExpansion);
		double res = U/T;
		return new Result(input, res, res*length*8);
	}
	
	/**
	 * Used only for testing, inputs are made up of exclusively either '0' or '1'.
	 *  
	 * @param prebuilt
	 * @return
	 */
	double calc(Character [] prebuilt) {
		
		// initial state
		U=T=0;
		binaryExpansion.clear();
		
		//collect();
		for(Character ch: prebuilt){
			binaryExpansion.add(ch);
		}
		
		compute(binaryExpansion);
		return U/T;
	}
	
	private String bitsAsString(int b) {
		String s = Integer.toBinaryString(b);
		if(s.length() > 8) throw new RuntimeException("Expecting to work with ascii range");
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
	
	public static class Result {
		
		public double biEntropy;
		public double bitsOfEntropy;
		private byte [] input;
		final DecimalFormat format = new DecimalFormat("#######0.00");
		
		public Result(byte [] input, double biEntropy, double bitsOfEntropy) {
			super();
			this.biEntropy = biEntropy;
			this.bitsOfEntropy = bitsOfEntropy;
			this.input = input;
		}

		@Override
		public String toString() {
			return "Result [biEntropy=" + format.format(biEntropy) + ", bitsOfEntropy="
					+ Math.round(bitsOfEntropy) + "]";
		}

		public String toJSON() {
			Map<String,Object> map = new LinkedHashMap<String,Object>();
			map.put("version", "Buttermilk BiEntropy v1.0");
			map.put("algorithm", "TresBiEntropy");
			if(input.length < 64) map.put("input", new String(input));
			if(input.length >= 64) map.put("input", new String(input).substring(0, 64)+"...");
			map.put("biEntropy", format.format(biEntropy));
			map.put("bitsOfEntropy", Math.round(bitsOfEntropy));
			ObjectMapper mapper = new ObjectMapper();
			mapper.enable(SerializationFeature.INDENT_OUTPUT);
			StringWriter writer = new StringWriter();
			try {
				mapper.writeValue(writer, map);
			} catch (JsonGenerationException e) {
				e.printStackTrace();
			} catch (JsonMappingException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
			return writer.toString();
		}

		@Override
		public int hashCode() {
			final int prime = 31;
			int result = 1;
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
			if (Double.doubleToLongBits(biEntropy) != Double
					.doubleToLongBits(other.biEntropy))
				return false;
			if (Double.doubleToLongBits(bitsOfEntropy) != Double
					.doubleToLongBits(other.bitsOfEntropy))
				return false;
			return true;
		}
		
	}
}
