/*
 *  This file is part of Buttermilk
 *  Copyright 2011-2015 David R. Smith All Rights Reserved.
 *
 */
package com.cryptoregistry.proto.frame;

import java.io.OutputStream;

public interface OutputFrame {

	public void writeFrame(OutputStream stream);
	
}
