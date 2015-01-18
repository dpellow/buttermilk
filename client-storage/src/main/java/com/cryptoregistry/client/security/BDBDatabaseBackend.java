/*
 *  This file is part of Buttermilk(TM) 
 *  Copyright 2013 David R. Smith for cryptoregistry.com
 *
 */
package com.cryptoregistry.client.security;

import com.sleepycat.je.Database;
import com.sleepycat.je.DatabaseException;

public interface BDBDatabaseBackend {

	/**
	 * Return the secure database
	 */
	public Database getSecureDatabase();

	public Database getMetadataDatabase();

	/**
	 * Close all databases and the environment.
	 */
	public void close() throws DatabaseException;

}