/*
 *  This file is part of Buttermilk
 *  Copyright 2011-2014 David R. Smith All Rights Reserved.
 *
 */
package com.cryptoregistry.pbe;

import java.util.Date;

import com.cryptoregistry.passwords.Password;

public class ArmoredPBKDF2Result extends ArmoredPBEResult {

	public final int iterations;
	
	public ArmoredPBKDF2Result(byte[] enc, byte[] salt, int iterations) {
		super(enc, salt);
		this.iterations = iterations;
	}

	public ArmoredPBKDF2Result(String base64Enc, String base64Salt, int iterations) {
		super(base64Enc, base64Salt);
		this.iterations = iterations;
	}
	
	public ArmoredPBKDF2Result(String version, Date createdOn, String base64Enc, String base64Salt, int iterations) {
		super(version, createdOn, base64Enc, base64Salt);
		this.iterations = iterations;
	}
	
	public PBEParams generateParams(Password password) {
		PBEParams params = new PBEParams(PBEAlg.PBKDF2);
		params.setPassword(password);
		params.setSalt(super.getSaltWrapper());
		params.setIterations(iterations);
		return params;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result + iterations;
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (!super.equals(obj))
			return false;
		if (getClass() != obj.getClass())
			return false;
		ArmoredPBKDF2Result other = (ArmoredPBKDF2Result) obj;
		if (iterations != other.iterations)
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "ArmoredPBKDF2Result [iterations=" + iterations + ", base64Enc="
				+ base64Enc + ", base64Salt=" + base64Salt + "]";
	}


}
