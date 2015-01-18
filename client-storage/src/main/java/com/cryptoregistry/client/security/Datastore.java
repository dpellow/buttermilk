/*
 *  This file is part of Buttermilk(TM) 
 *  Copyright 2015 David R. Smith for cryptoregistry.com
 *
 */
package com.cryptoregistry.client.security;

import com.sleepycat.je.DatabaseException;

public interface Datastore {

	public abstract void close() throws DatabaseException;

	public abstract BDBDatabaseBackend getDb();

	public abstract DatastoreViews getViews();

	public abstract String getRegHandle();

	public abstract KeyManager getSecurityManager();

}