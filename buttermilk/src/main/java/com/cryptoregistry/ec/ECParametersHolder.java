/*
 *  This file is part of Buttermilk
 *  Copyright 2011-2014 David R. Smith All Rights Reserved.
 *
 */
package com.cryptoregistry.ec;

import x.org.bouncycastle.crypto.params.ECDomainParameters;

public interface ECParametersHolder {
	 ECDomainParameters createParameters();
}
