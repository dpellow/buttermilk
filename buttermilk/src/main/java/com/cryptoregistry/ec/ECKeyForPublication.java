package com.cryptoregistry.ec;

import java.util.Date;
import java.util.UUID;

import com.cryptoregistry.formats.ec.JsonECKeyFormatter;

import x.org.bouncycastle.crypto.params.ECDomainParameters;
import x.org.bouncycastle.crypto.params.ECPublicKeyParameters;
import x.org.bouncycastle.math.ec.ECPoint;

public class ECKeyForPublication {

	public final String version;
	public final Date createdOn;
	
	public final String handle;
	public final ECPoint Q;
	public final String curveName;
	
	public ECKeyForPublication(String version, Date createdOn, String handle, ECPoint q, String curveName) {
		super();
		this.version = version;
		this.createdOn = createdOn;
		this.handle = handle;
		Q = q;
		this.curveName = curveName;
	}
	
	public ECKeyForPublication(ECPoint q, String curveName) {
		super();
		this.version = JsonECKeyFormatter.VERSION;  
		this.createdOn = new Date();
		this.handle = UUID.randomUUID().toString();
		Q = q;
		this.curveName = curveName;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((Q == null) ? 0 : Q.hashCode());
		result = prime * result
				+ ((curveName == null) ? 0 : curveName.hashCode());
		result = prime * result + ((handle == null) ? 0 : handle.hashCode());
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
		if (handle == null) {
			if (other.handle != null)
				return false;
		} else if (!handle.equals(other.handle))
			return false;
		return true;
	}
	
	public ECPublicKeyParameters getPublicKey() {
		ECDomainParameters domain = CurveFactory.getCurveForName(curveName);
		ECPublicKeyParameters p_params = new ECPublicKeyParameters(Q,domain);
		return p_params;
	}
	
	public final String toString() {
		return handle;
	}
}
