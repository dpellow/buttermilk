package com.cryptoregistry.ec;

import java.math.BigInteger;

import x.org.bouncycastle.math.ec.ECPoint;

public class ECKeyContents {
	
	public final BigInteger d;
	public final ECPoint Q;
	public String curveName;

	public ECKeyContents(String name, BigInteger d, ECPoint Q) {
		this.curveName = name;
		this.d = d;
		this.Q = Q;
	}

}
