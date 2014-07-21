/*
 *  This file is part of Buttermilk(TM) 
 *  Copyright 2013 David R. Smith for cryptoregistry.com
 *
 */
package com.cryptoregistry.client.storage;

import com.sleepycat.bind.EntryBinding;
import com.sleepycat.bind.serial.ClassCatalog;
import com.sleepycat.bind.serial.SerialBinding;
import com.sleepycat.collections.StoredSortedMap;


public class ButtermilkViews {

	private StoredSortedMap<SecureKey,SecureData> secureMap;
	
    /**
     * Create the data bindings and collection views.
     */
   
	public ButtermilkViews(ButtermilkDatabase db) {

        ClassCatalog catalog = db.getClassCatalog();
        
        EntryBinding<SecureKey> secureKeyBinding = new SerialBinding<SecureKey>(catalog,SecureKey.class);
        EntryBinding<SecureData> secureDataBinding = new SerialBinding<SecureData>(catalog, SecureData.class);
   
        secureMap = new StoredSortedMap<SecureKey,SecureData>(
				db.getSecureDatabase(),
				secureKeyBinding, 
				secureDataBinding, 
				true);
     
    }

	public StoredSortedMap<SecureKey, SecureData> getSecureMap() {
		return secureMap;
	}
  
}
