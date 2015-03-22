package com.cryptoregistry.mbean;

/**
 * You must set BUTTERMILK_HOME in your environment prior to using this class.
 * 
 * DatastoreManager loads the current datastore on start-up if BUTTERMILK_HOME is set and it points to 
 * an existing store. Otherwise you must use createDatastore() first prior to the other functions.
 * 
 * @author Dave
 *
 */
public interface DatastoreManagerMBean {
	
	/**
	 * Password file must be created in advance
	 * 
	 * @param regHandle
	 * @param pathToPasswordFile
	 */
	public void createDatastore(String regHandle, String pathToPasswordFile);
	

}
