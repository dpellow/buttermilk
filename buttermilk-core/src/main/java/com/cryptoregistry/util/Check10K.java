/*
 *  This file is part of Buttermilk(TM) 
 *  Copyright 2013-2014 David R. Smith for cryptoregistry.com
 *
 */
package com.cryptoregistry.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashSet;
import java.util.Set;

import x.org.bouncycastle.util.Arrays;

/**
 * Validate if a password is one of the known values people most often use based
 * on statistical analysis of a large list of password data. See:
 * https://xato.net/passwords/more-top-worst-passwords/#.U16Dk_mSySo
 * 
 * 
 * @author Dave
 * 
 */
public class Check10K {
	
	Set<String> passwords; 
	
	public Check10K() {
		passwords = new HashSet<String>(); 
		InputStream in = Thread.currentThread().getClass()
				.getResourceAsStream("/10k most common.txt");
		InputStreamReader reader = new InputStreamReader(in);
		BufferedReader bin = new BufferedReader(reader);
		String line = null;
		try {
			while ((line = bin.readLine()) != null) {
				passwords.add(line.trim());
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public boolean contains(String password){
		return passwords.contains(password);
	}

	/**
	 * Low memory form, just stream over the file
	 * 
	 * @param input
	 * @return
	 */
	public static final boolean isPresent(char[] input) {

		char[] buf = new char[input.length];
		for (int i = 0; i < input.length; i++) {
			char ch = input[i];
			buf[i] = Character.toLowerCase(ch);
		}

		InputStream in = Thread.currentThread().getClass()
				.getResourceAsStream("/10k most common.txt");
		InputStreamReader reader = new InputStreamReader(in);
		BufferedReader bin = new BufferedReader(reader);
		String line = null;
		try {
			while ((line = bin.readLine()) != null) {
				char[] testPat = line.toCharArray();
				if (Arrays.areEqual(testPat, buf))
					return true;
			}
		} catch (IOException e) {
			e.printStackTrace();
		} finally{
			try {
				if(in != null) in.close();
			} catch (IOException e) {}
		}

		return false;
	}

	/**
	 * Low memory format, just stream over the file
	 * 
	 * @param input
	 * @return
	 */
	public static final boolean isPresent(String input) {

		String lower = input.toLowerCase();
		InputStream in = Thread.currentThread().getClass()
				.getResourceAsStream("/10k most common.txt");
		InputStreamReader reader = new InputStreamReader(in);
		BufferedReader bin = new BufferedReader(reader);
		String line = null;
		try {
			while ((line = bin.readLine()) != null) {
				if (lower.equals(line))
					return true;
			}
		} catch (IOException e) {
			e.printStackTrace();
		}

		return false;
	}

}
