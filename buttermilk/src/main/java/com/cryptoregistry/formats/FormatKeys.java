/*
 *  This file is part of Buttermilk
 *  Copyright 2011-2014 David R. Smith All Rights Reserved.
 *
 */
package com.cryptoregistry.formats;

import java.io.Writer;

public interface FormatKeys {
	public void formatKeys(Mode mode, Encoding enc, Writer writer);
}
