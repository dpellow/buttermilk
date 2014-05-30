/*
 *  This file is part of Buttermilk(TM) 
 *  Copyright 2013-2014 David R. Smith for cryptoregistry.com
 *
 */
package com.cryptoregistry.passwords;

import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.charset.Charset;
import java.util.Arrays;

import com.cryptoregistry.util.Check10K;
import com.cryptoregistry.util.entropy.ShannonEntropy;
import com.cryptoregistry.util.entropy.TresBiEntropy;

/**
 * Base class - wrapper for a char array - a password value - we intend to remove 
 * from heap memory as soon as is feasible, i.e., manually. This is done
 * to protect from leaks in heap dumps, etc. It is not possible to completely
 * remove such leaks - but we can try. 
 * 
 * @author Dave
 *
 */
public class Password {
	
	protected char [] password;
	protected boolean alive;
	
	public Password(char[] password) {
		super();
		this.password = password;
		alive=true;
	}
	
	/**
	 * Add the contents of two passwords together into one (concatenate p1 and p2)
	 * @param p1
	 * @param p2
	 */
	public Password(Password p1, Password p2) {
		super();
		int length = p1.length()+p2.length();
		password = new char[length];
		System.arraycopy(p1.getPassword(), 0, password, 0, p1.length());
		System.arraycopy(p2.getPassword(), 0, password, p1.length(), p2.length());
		alive=true;
	}

	public char [] getPassword(){
		if(!alive) throw new RuntimeException("This Password instance is dead...");
		return password;
	}
	
	public void selfDestruct(){
		for(int i = 0; i<password.length; i++){
			password[i] = '\0';
		}
		alive = false;
	}
	
	public int length() {
		return password.length;
	}
	
	/**
	 * This makes a copy of the characters as bytes
	 * @return
	 */
	public byte[] toBytes() {
	    CharBuffer charBuffer = CharBuffer.wrap(password);
	    ByteBuffer byteBuffer = Charset.forName("UTF-8").encode(charBuffer);
	    byte[] bytes = Arrays.copyOfRange(byteBuffer.array(),
	            byteBuffer.position(), byteBuffer.limit());
	    Arrays.fill(charBuffer.array(), '\u0000'); // clear sensitive data
	    Arrays.fill(byteBuffer.array(), (byte) 0); // clear sensitive data
	    return bytes;
	}

	public boolean isAlive() {
		return alive;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + (alive ? 1231 : 1237);
		result = prime * result + Arrays.hashCode(password);
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
		Password other = (Password) obj;
		if (alive != other.alive)
			return false;
		if (!Arrays.equals(password, other.password))
			return false;
		return true;
	}
	
	/**
	 * Returns the bits of entropy calculated with the Shannon Method. 
	 * The Croll method (bientropy), below is much more accurate for keys
	 * 
	 * @return
	 */
	public int shannonEntropy(){
		return ShannonEntropy.bitsOfEntropy(password);
	}
	
	public boolean isPresent10k() {
		return Check10K.isPresent(password);
	}
	
	/**
	 * Calculates the bits of entropy using the Croll method (bientropy).
	 * 
	 * @return
	 */
	public int biEntropy() {
		TresBiEntropy bi = new TresBiEntropy(toBytes());
		return (int) bi.calc().bitsOfEntropy;
	}

}
