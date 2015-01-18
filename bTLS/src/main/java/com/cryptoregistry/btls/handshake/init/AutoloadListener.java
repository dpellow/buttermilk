/*
 *  This file is part of Buttermilk
 *  Copyright 2011-2014 David R. Smith All Rights Reserved.
 *
 */
package com.cryptoregistry.btls.handshake.init;

/**
 * Listeners can be notified of asynchronous completion of the load (i.e., the initialization of the components used by
 * a given protocol has been completed). 
 *  
 * @author Dave
 *
 */
public interface AutoloadListener {
	
	public void autoloadCompleted(AutoloadEvent evt);

}
