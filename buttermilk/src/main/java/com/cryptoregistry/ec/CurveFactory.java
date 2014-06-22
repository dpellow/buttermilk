/*
 *  This file is part of Buttermilk
 *  Copyright 2011-2014 David R. Smith All Rights Reserved.
 *
 */
package com.cryptoregistry.ec;

import x.org.bouncycastle.crypto.params.ECDomainParameters;

/**
 * Static definitions of the various curves. These are adapted from Bouncy Castle's definitions
 * 
 * TODO yet: the certicom curves over F2m
 * 
 * @author Dave
 *
 */
public class CurveFactory {
	
	public static boolean curveNameDefined(final String name){
		try {
			return getCurveForName(name) != null;
		}catch(RuntimeException x){
			return false;
		}
	}
	
	public static ECDomainParameters getCurveForName(final String name){
		switch(name){
			case "secp112r1":{
				return CurveFactory.secp112r1.createParameters();
			}
			case "secp112r2":{
				return CurveFactory.secp112r2.createParameters();
			}
			case "secp128r1":{
				return CurveFactory.secp128r1.createParameters();
			}
			case "secp128r2":{
				return CurveFactory.secp128r2.createParameters();
			}
			case "secp160k1":{
				return CurveFactory.secp160k1.createParameters();
			}
			case "secp160r1":{
				return CurveFactory.secp160r1.createParameters();
			}
			case "secp160r2":{
				return CurveFactory.secp160r2.createParameters();
			}
			case "secp192k1":{
				return CurveFactory.secp192k1.createParameters();
			}
			case "secp192r1":{
				return CurveFactory.secp192r1.createParameters();
			}
			case "secp224k1":{
				return CurveFactory.secp224k1.createParameters();
			}
			case "secp224r1":{
				return CurveFactory.secp224r1.createParameters();
			}
			case "secp256k1":{
				return CurveFactory.secp256k1.createParameters();
			}
			
			//NIST P-256
			
			case "secp256r1":{
				return CurveFactory.secp256r1.createParameters();
			}
			case "P256":{
				return CurveFactory.p256.createParameters();
			}
			case "P-256":{
				return CurveFactory.p256Dash.createParameters();
			}
			
			//NIST P-384
			
			case "secp284r1":{
				return CurveFactory.secp384r1.createParameters();
			}
			case "P384":{
				return CurveFactory.p384.createParameters();
			}
			case "P-384":{
				return CurveFactory.p384Dash.createParameters();
			}
		
			
			//NIST P-521
			
			case "secp521r1":{
				return CurveFactory.secp521r1.createParameters();
			}
			case "P521":{
				return CurveFactory.p521.createParameters();
			}
			case "P-521":{
				return CurveFactory.p521Dash.createParameters();
			}
			
			
			case "brainpoolP160r1": {
				return CurveFactory.brainpoolP160r1.createParameters();
			}
			
			// TODO add all the rest
			
			default: throw new RuntimeException("unknown curve name: "+name);
		}
	}
	
	private static final ECParametersHolder secp112r1 = Secp112r1.instance();
	private static final ECParametersHolder secp112r2 = Secp112r2.instance();
	private static final ECParametersHolder secp128r1 = Secp128r1.instance();
	private static final ECParametersHolder secp128r2 = Secp128r2.instance();
	private static final ECParametersHolder secp160k1 = Secp160k1.instance();
	private static final ECParametersHolder secp160r1 = Secp160r1.instance();
	private static final ECParametersHolder secp160r2 = Secp160r2.instance();
	private static final ECParametersHolder secp192k1 = Secp192k1.instance();
	private static final ECParametersHolder secp192r1 = Secp192r1.instance();
	
	private static final ECParametersHolder secp224k1 = Secp224k1.instance();
	private static final ECParametersHolder secp224r1 = Secp224r1.instance();
	private static final ECParametersHolder secp256k1 = Secp256k1.instance();
	
	private static final ECParametersHolder secp256r1 = Secp256r1.instance();
	private static final ECParametersHolder p256 = Secp256r1.p256();
	private static final ECParametersHolder p256Dash = Secp256r1.p256Dash();
	
	private static final ECParametersHolder secp384r1 = Secp384r1.instance();
	private static final ECParametersHolder p384 = Secp384r1.p384();
	private static final ECParametersHolder p384Dash = Secp384r1.p384Dash();
	
	private static final ECParametersHolder secp521r1 = Secp521r1.instance();
	private static final ECParametersHolder p521 = Secp521r1.p521();
	private static final ECParametersHolder p521Dash = Secp521r1.p521Dash();
	
	
	private static final ECParametersHolder brainpoolP160r1 = BrainpoolP160r1.instance();
	
}
