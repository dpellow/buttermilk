/*
 *  This file is part of Buttermilk
 *  Copyright 2011-2014 David R. Smith All Rights Reserved.
 *
 */
package com.cryptoregistry.btls.handshake;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.cryptoregistry.client.security.Datastore;

public class BasicHandshake extends Handshake {

	static final Logger logger = LogManager.getLogger(BasicHandshake.class
			.getName());

	private BasicHandshake() {
		super();
	}

	/**
	 * Clients use this
	 * 
	 * @param hp
	 * @param ds
	 */
	public BasicHandshake(HandshakeProtocol hp, Datastore ds) {
		this();
		this.server = false;
		this.ds = ds;
		this.hp = hp;
		logger.trace("In Constructor for Client Handshake...");
	}

	/**
	 * Servers use this, handshake protocol is sent via the client
	 * 
	 * @param ds
	 */
	public BasicHandshake(Datastore ds) {
		this();
		this.server = true;
		this.ds = ds;
		logger.trace("In Constructor for Server Handshake...");
	}

	@Override
	public void doHandshake() throws HandshakeFailedException {

		if (this.isServer()) {
			try {

				logger.trace("Entering Autoloader.load()");
				autoloader.load();
				logger.trace("Exiting Autoloader.load()");

				logger.trace("Entering KEM.exchange()");
				kem.exchange();
				logger.trace("Exiting KEM.exchange()");

				validator.validate();
				ekem.exchange();
			} catch (Exception e) {
				throw new HandshakeFailedException(e);
			}
		} else {
			try {

				logger.trace("calling start protocol...");
				startProtocol();

				logger.trace("Entering Client Autoloader.load()");
				autoloader.load();
				logger.trace("Exiting Client Autoloader.load()");

				logger.trace("Entering Client KEM.exchange()");
				kem.exchange();
				logger.trace("Exiting Client KEM.exchange()");

				validator.validate();
				ekem.exchange();

			} catch (Exception e) {
				throw new HandshakeFailedException(e);
			}
		}
	}
}
