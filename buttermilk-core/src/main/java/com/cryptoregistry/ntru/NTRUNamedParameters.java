/*
 *  This file is part of Buttermilk
 *  Copyright 2011-2014 David R. Smith All Rights Reserved.
 *
 */
package com.cryptoregistry.ntru;

import x.org.bouncycastle.pqc.crypto.ntru.NTRUEncryptionKeyGenerationParameters;
import x.org.bouncycastle.pqc.crypto.ntru.NTRUEncryptionParameters;

/**
 * A little bit like NamedCurves in the EC package
 * 
 * @author Dave
 *
 */
public enum NTRUNamedParameters {
	
	EES1087EP2,
	EES1171EP1,
	EES1499EP1,
	APR2011_439,
	APR2011_439_FAST,
	APR2011_743,
	APR2011_743_FAST;
	
	public NTRUEncryptionParameters getParameters() {
		
		switch(this.name()){
			case "EES1087EP2": {
				return NTRUEncryptionKeyGenerationParameters.EES1087EP2.getEncryptionParameters();
			}
			case "EES1171EP1": {
				return NTRUEncryptionKeyGenerationParameters.EES1171EP1.getEncryptionParameters();
			}
			case "EES1499EP1": {
				return NTRUEncryptionKeyGenerationParameters.EES1499EP1.getEncryptionParameters();
			}
			case "APR2011_439": {
				return NTRUEncryptionKeyGenerationParameters.APR2011_439.getEncryptionParameters();
			}
			case "APR2011_439_FAST": {
				return NTRUEncryptionKeyGenerationParameters.APR2011_439_FAST.getEncryptionParameters();
			}
			case "APR2011_743": {
				return NTRUEncryptionKeyGenerationParameters.APR2011_743.getEncryptionParameters();
			}
			case "APR2011_743_FAST": {
				return NTRUEncryptionKeyGenerationParameters.APR2011_743_FAST.getEncryptionParameters();
			}
		}
		
		throw new RuntimeException("Unknown Parameter name");
	}
	
	public NTRUEncryptionKeyGenerationParameters getKeyGenerationParameters() {
		
		switch(this.name()){
			case "EES1087EP2": {
				return NTRUEncryptionKeyGenerationParameters.EES1087EP2;
			}
			case "EES1171EP1": {
				return NTRUEncryptionKeyGenerationParameters.EES1171EP1;
			}
			case "EES1499EP1": {
				return NTRUEncryptionKeyGenerationParameters.EES1499EP1;
			}
			case "APR2011_439": {
				return NTRUEncryptionKeyGenerationParameters.APR2011_439;
			}
			case "APR2011_439_FAST": {
				return NTRUEncryptionKeyGenerationParameters.APR2011_439_FAST;
			}
			case "APR2011_743": {
				return NTRUEncryptionKeyGenerationParameters.APR2011_743;
			}
			case "APR2011_743_FAST": {
				return NTRUEncryptionKeyGenerationParameters.APR2011_743_FAST;
			}
		}
		
		throw new RuntimeException("Unknown Parameter name");
	}
}
