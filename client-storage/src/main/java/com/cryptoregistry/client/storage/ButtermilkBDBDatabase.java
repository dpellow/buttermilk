/*
 *  This file is part of Buttermilk(TM) 
 *  Copyright 2013 David R. Smith for cryptoregistry.com
 *
 */
package com.cryptoregistry.client.storage;

import java.io.File;

import com.sleepycat.bind.serial.ClassCatalog;
import com.sleepycat.bind.serial.SerialSerialKeyCreator;
import com.sleepycat.bind.serial.StoredClassCatalog;
import com.sleepycat.je.Database;
import com.sleepycat.je.DatabaseConfig;
import com.sleepycat.je.DatabaseException;
import com.sleepycat.je.Environment;
import com.sleepycat.je.EnvironmentConfig;
import com.sleepycat.je.SecondaryConfig;
import com.sleepycat.je.SecondaryDatabase;
import com.cryptoregistry.client.security.BDBDatabaseBackend;
import com.cryptoregistry.passwords.SensitiveBytes;

/**
 * ButtermilkDatabase defines the storage containers, indices and foreign keys
 * for the secure database.
 *
 * @author Dave Smith
 */
public class ButtermilkBDBDatabase implements BDBDatabaseBackend {

	private static final String CLASS_CATALOG = "java_class_catalog";
	private static final String SecureStore = "secure_store";
	private static final String MetadataStore = "metadata_store";
	private static final String RegHandleIndex = "reg_handle_index";

	private final Environment env;
	private final Database secureDb;
	private final Database metadataDb;

	private final SecondaryDatabase regHandleDb; // find records by RegHandle

	private final StoredClassCatalog javaCatalog;

	// not used currently may be useful in a subclass
	protected final SensitiveBytes cachedKey;

	/**
	 * Open all storage containers, indices, and catalogs.
	 */
	public ButtermilkBDBDatabase(String homeDirectory, SensitiveBytes cachedKey)
			throws DatabaseException {

		this.cachedKey = cachedKey;

		// Open the Berkeley DB environment in transactional mode.
		//
		System.out.println("Opening environment in: " + homeDirectory);
		EnvironmentConfig envConfig = new EnvironmentConfig();
		envConfig.setTransactional(true);
		envConfig.setAllowCreate(true);
		env = new Environment(new File(homeDirectory), envConfig);

		// Set the Berkeley DB config for opening all stores.
		//
		DatabaseConfig dbConfig = new DatabaseConfig();
		dbConfig.setTransactional(true);
		dbConfig.setAllowCreate(true);

		// Create the Serial class catalog. This holds the serialized class
		// format for all database records of serial format.
		//
		Database catalogDb = env.openDatabase(null, CLASS_CATALOG, dbConfig);
		javaCatalog = new StoredClassCatalog(catalogDb);

		// Open the Berkeley DB database
		//
		secureDb = env.openDatabase(null, SecureStore, dbConfig);
		metadataDb = env.openDatabase(null, MetadataStore, dbConfig);

		// now do secondary index

		SecondaryConfig secConfig = new SecondaryConfig();
		secConfig.setTransactional(true);
		secConfig.setAllowCreate(true);
		secConfig.setSortedDuplicates(true); // allow duplicates
		secConfig.setKeyCreator(new RegHandleKeyCreator(javaCatalog,
				Handle.class, Metadata.class, Handle.class));

		regHandleDb = env.openSecondaryDatabase(null, RegHandleIndex,
				metadataDb, secConfig);

	}

	/**
	 * Return the storage environment for the database.
	 */
	public final Environment getEnvironment() {

		return env;
	}

	/**
	 * Return the class catalog.
	 */
	public final StoredClassCatalog getClassCatalog() {

		return javaCatalog;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.cryptoregistry.client.storage.DatabaseBackend#getSecureDatabase()
	 */
	@Override
	public final Database getSecureDatabase() {
		return secureDb;
	}

	// public final Database getKeyGenAlgDatabase() {
	// return keyGenAlgDb;
	// }

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.cryptoregistry.client.storage.DatabaseBackend#getMetadataDatabase()
	 */
	@Override
	public final Database getMetadataDatabase() {
		return metadataDb;
	}

	public final SecondaryDatabase getRegHandleDatabase() {
		return regHandleDb;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.cryptoregistry.client.storage.DatabaseBackend#close()
	 */
	@Override
	public void close() throws DatabaseException {

		// secondary
		regHandleDb.close();
		// primary
		metadataDb.close();
		secureDb.close();
		javaCatalog.close();
		env.close();
	}

	@SuppressWarnings({ "rawtypes" })
	private static class RegHandleKeyCreator extends SerialSerialKeyCreator {

		@SuppressWarnings("unchecked")
		public RegHandleKeyCreator(ClassCatalog classCatalog,
				Class primaryKeyClass, Class dataClass, Class indexKeyClass) {
			super(classCatalog, primaryKeyClass, dataClass, indexKeyClass);
			// TODO Auto-generated constructor stub
		}

		@Override
		public Object createSecondaryKey(Object primaryKey, Object data) {

			Metadata meta = (Metadata) data;
			return new Handle(meta.getRegistrationHandle());
		}
	}

}
