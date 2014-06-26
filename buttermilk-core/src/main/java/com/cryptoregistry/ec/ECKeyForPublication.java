/*
 *  This file is part of Buttermilk
 *  Copyright 2011-2014 David R. Smith All Rights Reserved.
 *
 */
package com.cryptoregistry.ec;

import java.util.Date;

import com.cryptoregistry.CryptoKeyMetadata;
import com.cryptoregistry.ECCustomCurve;
import com.cryptoregistry.KeyGenerationAlgorithm;
import com.cryptoregistry.Verifier;
import com.cryptoregistry.formats.KeyFormat;

import x.org.bouncycastle.crypto.params.ECDomainParameters;
import x.org.bouncycastle.crypto.params.ECPublicKeyParameters;
import x.org.bouncycastle.math.ec.ECPoint;

/**
 * An elliptic curve "public key."
 * 
 * Custom Curve support:
 * 
 * if "customCurve" is null, then customCurveDefinition will be defined and vice versa.
 * 
 * @author Dave
 *
 */
public class ECKeyForPublication  implements CryptoKeyMetadata,Verifier {

	public final ECKeyMetadata metadata;
	public final ECPoint Q;
	public final String curveName;  // if defined, then customCurveDefinition must equal null
	public final ECCustomCurve customCurveDefinition;
	
	public ECKeyForPublication(ECKeyMetadata meta, ECPoint q, String curveName) {
		super();
		this.metadata = meta;
		Q = q;
		this.curveName = curveName;
		this.customCurveDefinition = null;
	}
	
	public ECKeyForPublication(ECKeyMetadata meta, ECPoint q, ECCustomCurve customCurveDefinition) {
		super();
		this.metadata = meta;
		Q = q;
		this.curveName = null;
		this.customCurveDefinition = customCurveDefinition;
	}
	
	public boolean usesNamedCurve() {
		return curveName != null;
	}
	
	public ECPublicKeyParameters getPublicKey() {
		if(usesNamedCurve()){
			ECDomainParameters domain = CurveFactory.getCurveForName(curveName);
			ECPublicKeyParameters p_params = new ECPublicKeyParameters(Q,domain);
			return p_params;
		}else{
			ECDomainParameters domain = this.customCurveDefinition.getParameters();
			ECPublicKeyParameters p_params = new ECPublicKeyParameters(Q,domain);
			return p_params;
		}
	}
	
	public ECKeyForPublication clone(){
		ECKeyMetadata meta = metadata.clone();
		if(usesNamedCurve()) return new ECKeyForPublication(meta,Q,curveName);
		else return new ECKeyForPublication(meta,Q,customCurveDefinition);
	}
	
	public ECKeyForPublication clone(KeyFormat format){
		ECKeyMetadata meta = new ECKeyMetadata(this.getHandle(),new Date(this.getCreatedOn().getTime()),format);
		if(usesNamedCurve()) return new ECKeyForPublication(meta,Q,curveName);
		else return new ECKeyForPublication(meta,Q,customCurveDefinition);
	}
	
	// delegate

	public String getHandle() {
		return metadata.getHandle();
	}
	
	public String getDistinguishedHandle() {
		return metadata.handle+"-"+metadata.format.mode.code;
	}

	public KeyGenerationAlgorithm getKeyAlgorithm() {
		return metadata.getKeyAlgorithm();
	}

	public Date getCreatedOn() {
		return metadata.getCreatedOn();
	}

	public KeyFormat getFormat() {
		return metadata.getFormat();
	}

	public ECCustomCurve getCustomCurveDefinition() {
		return customCurveDefinition;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((Q == null) ? 0 : Q.hashCode());
		result = prime * result
				+ ((curveName == null) ? 0 : curveName.hashCode());
		result = prime * result
				+ ((metadata == null) ? 0 : metadata.hashCode());
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
		if (metadata == null) {
			if (other.metadata != null)
				return false;
		} else if (!metadata.equals(other.metadata))
			return false;
		return true;
	}
}
