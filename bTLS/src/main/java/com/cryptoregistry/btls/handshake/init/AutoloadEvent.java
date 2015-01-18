/*
 *  This file is part of Buttermilk
 *  Copyright 2011-2014 David R. Smith All Rights Reserved.
 *
 */
package com.cryptoregistry.btls.handshake.init;

import java.util.EventObject;

import com.cryptoregistry.btls.handshake.Handshake;

/**
 * Event is generated once initialization of the handshake protocol's components is completed.
 * 
 * @author Dave
 *
 */
public class AutoloadEvent extends EventObject {

	private static final long serialVersionUID = 1L;
	
	Handshake handshake;

	public AutoloadEvent(Object source, Handshake handshake) {
		super(source);
		this.handshake = handshake;
	}

	public Handshake getHandshake() {
		return handshake;
	}

}
