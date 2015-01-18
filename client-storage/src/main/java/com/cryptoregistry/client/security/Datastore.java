package com.cryptoregistry.client.security;

import com.cryptoregistry.client.storage.ButtermilkBDBDatabase;
import com.cryptoregistry.client.storage.ButtermilkViews;
import com.sleepycat.je.DatabaseException;

public interface Datastore {

	public abstract void close() throws DatabaseException;

	public abstract ButtermilkBDBDatabase getDb();

	public abstract DatastoreViews getViews();

	public abstract String getRegHandle();

	public abstract KeyManager getSecurityManager();

}