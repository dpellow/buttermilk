package com.cryptoregistry.ec;

import x.org.bouncycastle.crypto.params.ECDomainParameters;

public interface ECParametersHolder {

	 ECDomainParameters createParameters();
}
