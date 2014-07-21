/*
 *  This file is part of Buttermilk(TM) 
 *  Copyright 2013 David R. Smith for cryptoregistry.com
 *
 */
package com.cryptoregistry.client.storage;

import java.io.File;

import com.sleepycat.bind.serial.StoredClassCatalog;
import com.sleepycat.je.Database;
import com.sleepycat.je.DatabaseConfig;
import com.sleepycat.je.DatabaseException;
import com.sleepycat.je.Environment;
import com.sleepycat.je.EnvironmentConfig;

/**
 * ButtermilkDatabase defines the storage containers, indices and foreign keys for the database.
 *
 * @author Dave Smith
 */
public class ButtermilkDatabase {

    private static final String CLASS_CATALOG = "java_class_catalog";
    private static final String SecureStore = "secure_store";

    private Environment env;
    private Database secureDb;
    
//    private SecondaryDatabase keysByRegDb; // keys by Registration (SecretKeeper). Given a registration handle, get his keys
    
    private StoredClassCatalog javaCatalog;

    /**
     * Open all storage containers, indices, and catalogs.
     */
    public ButtermilkDatabase(String homeDirectory)
        throws DatabaseException {

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

        // Create the Serial class catalog.  This holds the serialized class
        // format for all database records of serial format.
        //
        Database catalogDb = env.openDatabase(null, CLASS_CATALOG, dbConfig);
        javaCatalog = new StoredClassCatalog(catalogDb);

        // Open the Berkeley DB database for the part, supplier and shipment
        // stores.  The stores are opened with no duplicate keys allowed.
        //
        secureDb = env.openDatabase(null, SecureStore, dbConfig);
        
        // now do secondary indexes
        
   //     SecondaryConfig secConfig = new SecondaryConfig();
   //     secConfig.setTransactional(true);
   //     secConfig.setAllowCreate(true);
   //     secConfig.setSortedDuplicates(true);
   //     secConfig.setKeyCreator(new KeysByRegKeyCreator(javaCatalog, KeysData.class));
   //     keysByRegDb = env.openSecondaryDatabase(null, KEYS_BY_REG_INDEX, keysDb, secConfig);
        
               
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
    
    /**
     * Return the password storage container.
     */
    public final Database getSecureDatabase() {

        return secureDb;
    }

    /**
     * Close all databases and the environment.
     */
    public void close() throws DatabaseException {
    	
    	// secondary
    	
    	// primary
    	secureDb.close();
        javaCatalog.close();
        env.close();
    }
    
 
    /*
	private static class KeysByRegKeyCreator extends TupleSerialKeyCreator {

        @SuppressWarnings("unchecked")
		private KeysByRegKeyCreator(ClassCatalog catalog, Class valueClass) {
            super(catalog, valueClass);
        }

        public boolean createSecondaryKey(TupleInput primaryKeyInput,
                                          Object valueInput,
                                          TupleOutput indexKeyOutput) {

            KeysData keyData = (KeysData) valueInput;
            String regHandle = keyData.get(CryptoAttribute.SecretKeeper);
            if (regHandle != null) {
                indexKeyOutput.writeString(regHandle);
                return true;
            } else {
                return false;
            }
        }
    }
    */
    
}
