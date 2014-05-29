package com.cryptoregistry.ec;

import java.math.BigInteger;
import java.util.Date;
import java.util.UUID;

import com.cryptoregistry.formats.ec.JsonECKeyFormatter;

import x.org.bouncycastle.crypto.params.ECDomainParameters;
import x.org.bouncycastle.crypto.params.ECPrivateKeyParameters;
import x.org.bouncycastle.math.ec.ECPoint;

public class ECKeyContents extends ECKeyForPublication {
	
	public final BigInteger d;
	
	/**
	 * auto-sets version, date, and handle
	 * 
	 * @param q
	 * @param curveName
	 * @param d
	 */
	public ECKeyContents(ECPoint q, String curveName, BigInteger d) {
		super(JsonECKeyFormatter.VERSION, new Date(), UUID.randomUUID().toString(), q, curveName);
		this.d = d;
	}

	public ECKeyContents(String version, Date createdOn, 
			String handle, ECPoint q, String curveName, BigInteger d) {
		super(version,createdOn,handle, q, curveName);
		this.d = d;
	}
	
	public ECPrivateKeyParameters getPrivateKey() {
		ECDomainParameters domain = CurveFactory.getCurveForName(curveName);
		 ECPrivateKeyParameters params = new ECPrivateKeyParameters(d, domain);
		 return params;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result + ((d == null) ? 0 : d.hashCode());
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
		ECKeyContents other = (ECKeyContents) obj;
		if (d == null) {
			if (other.d != null)
				return false;
		} else if (!d.equals(other.d))
			return false;
		return true;
	}

}
