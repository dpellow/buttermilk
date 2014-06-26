package com.cryptoregistry;

import x.org.bouncycastle.crypto.params.ECDomainParameters;

/**
 * Classes which implement this method can function as custom curve definitions (as opposed
 * to named curves such as NIST P-256) for Elliptic Curve cryptography
 * 
 * @author Dave
 * @see ECCustomParameters
 * 
 */
public interface ECCustomCurve {
	public ECDomainParameters getParameters();
}
