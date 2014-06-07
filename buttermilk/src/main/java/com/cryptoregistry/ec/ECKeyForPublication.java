/*
 *  This file is part of Buttermilk
 *  Copyright 2011-2014 David R. Smith All Rights Reserved.
 *
 */
package com.cryptoregistry.ec;

import java.util.Date;
import com.cryptoregistry.CryptoKeyMetadata;
import com.cryptoregistry.KeyGenerationAlgorithm;
import com.cryptoregistry.formats.KeyFormat;

import x.org.bouncycastle.crypto.params.ECDomainParameters;
import x.org.bouncycastle.crypto.params.ECPublicKeyParameters;
import x.org.bouncycastle.math.ec.ECPoint;

public class ECKeyForPublication  implements CryptoKeyMetadata {

	private final ECKeyMetadata management;
	public final ECPoint Q;
	public final String curveName;
	
	public ECKeyForPublication(ECKeyMetadata management, ECPoint q, String curveName) {
		super();
		this.management = management;
		Q = q;
		this.curveName = curveName;
	}
	
	public ECPublicKeyParameters getPublicKey() {
		ECDomainParameters domain = CurveFactory.getCurveForName(curveName);
		ECPublicKeyParameters p_params = new ECPublicKeyParameters(Q,domain);
		return p_params;
	}
	
	// delegate

	public String getHandle() {
		return management.getHandle();
	}

	public KeyGenerationAlgorithm getKeyAlgorithm() {
		return management.getKeyAlgorithm();
	}

	public Date getCreatedOn() {
		return management.getCreatedOn();
	}

	public KeyFormat getFormat() {
		return management.getFormat();
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((Q == null) ? 0 : Q.hashCode());
		result = prime * result
				+ ((curveName == null) ? 0 : curveName.hashCode());
		result = prime * result
				+ ((management == null) ? 0 : management.hashCode());
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
		ECKeyForPublication other = (ECKeyForPublication) obj;
		if (Q == null) {
			if (other.Q != null)
				return false;
		} else if (!Q.equals(other.Q))
			return false;
		if (curveName == null) {
			if (other.curveName != null)
				return false;
		} else if (!curveName.equals(other.curveName))
			return false;
		if (management == null) {
			if (other.management != null)
				return false;
		} else if (!management.equals(other.management))
			return false;
		return true;
	}
}
