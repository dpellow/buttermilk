/*
 *  This file is part of Buttermilk
 *  Copyright 2011-2015 David R. Smith All Rights Reserved.
 *
 */
package com.cryptoregistry.util;

import java.util.ArrayList;
import java.util.Random;

/**
 * Strings are not of cryptographic strength in terms of randomness.
 * 
 * @author Dave
 *
 */
public class RandomStringGenerator {

	Random rand = new Random();
	final static String alphabet = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ1234567890";

	public RandomStringGenerator() {

	}

	public String nextString(int length) {
		StringBuffer buf = new StringBuffer();

		for (int i = 0; i < length; i++) {
			buf.append(alphabet.charAt(rand.nextInt(alphabet.length())));
		}

		return buf.toString();
	}
	
	public ArrayList<String> next(int lineLength, int lines) {
		
		ArrayList<String> array = new ArrayList<String>();
		
		for(int j = 0; j<lines;j++){
			StringBuffer buf = new StringBuffer();
			for (int i = 0; i < lineLength; i++) {
				buf.append(alphabet.charAt(rand.nextInt(alphabet.length())));
			}
			array.add(buf.toString());
		}

		return array;
		
	}

}
