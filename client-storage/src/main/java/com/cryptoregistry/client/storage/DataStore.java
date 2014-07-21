package com.cryptoregistry.client.storage;

import java.io.File;

import com.sleepycat.je.DatabaseException;

/**
 * Create a database
 * 
 * @author Dave
 *
 */
public class DataStore {

	private static final String DEFAULT_DB_DATABASE_FOLDERNAME = "buttermilk-db";

	protected ButtermilkDatabase db;
	protected ButtermilkViews views;

	/**
	 * Will put the store into <user.home>/buttermilk-db
	 */
	public DataStore() {
		initDb(defaultDBFolder());
	}
	
	/**
	 * alternateLoc should be a full path to a directory for the store
	 * 
	 * @param alternateLoc
	 */
	public DataStore(String alternateLoc) {
		initDb(alternateLoc);
	}

	protected void initDb(String dataHomeDir) throws DatabaseException {

		File dbPathFile = new File(dataHomeDir);
		if (!dbPathFile.exists()) {
			dbPathFile.mkdirs();
		}

		db = new ButtermilkDatabase(dataHomeDir);
		views = new ButtermilkViews(db);
	}

	protected String defaultDBFolder() {
		StringBuffer buf = new StringBuffer();
		String userHome = System.getProperties().getProperty("user.home");
		buf.append(userHome);
		buf.append(File.separator);
		buf.append(DEFAULT_DB_DATABASE_FOLDERNAME);

		return buf.toString();
	}

	public void closeDb() throws DatabaseException {
		db.close();
	}

	public ButtermilkDatabase getDb() {
		return db;
	}

	public ButtermilkViews getViews() {
		return views;
	}

}
