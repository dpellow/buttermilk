/*
 *  This file is part of Buttermilk
 *  Copyright 2011-2014 David R. Smith All Rights Reserved.
 *
 */
package com.cryptoregistry.ec;

import java.math.BigInteger;

import x.org.bouncycastle.crypto.params.ECDomainParameters;
import x.org.bouncycastle.util.encoders.Hex;

public abstract class ECParametersHolderBase implements ECParametersHolder {

	protected final ECDomainParameters cached;
	
	public ECParametersHolderBase(ECDomainParameters params) {
		this.cached = params;
	}

	@Override
	public abstract ECDomainParameters createParameters();

	
	protected static BigInteger fromHex(String hex) {
		return new BigInteger(1, Hex.decode(hex));
	}

}
