package com.cryptoregistry.client.storage;

import java.io.File;

import asia.redact.bracket.properties.Properties;

import com.cryptoregistry.client.security.SecurityManagerInterface;
import com.cryptoregistry.passwords.SensitiveBytes;
import com.sleepycat.je.DatabaseException;

/**
 * Create a datastore for key data. The store will be encrypted using two factor security. to
 * prepare for this you will need a thumb drive (flash drive) and run the twofactor.sh script
 * 
 * @author Dave
 * 
 */
public class DataStore {

	protected ButtermilkDatabase db;
	protected ButtermilkViews views;
	protected Properties props;
	protected SecurityManagerInterface securityManagerInterface;
	protected String regHandle;
	
	
	public DataStore(Properties props, SecurityManagerInterface securityManagerInterface) {
		this.props = props;
		this.securityManagerInterface = securityManagerInterface;
		
		SensitiveBytes cachedKey = securityManagerInterface.loadKey(securityManagerInterface.getPassword());
		
		if(!props.containsKey("buttermilk.datastore.home")){
			throw new RuntimeException("Please define buttermilk.datastore.home in your properties");
		}
		String dbHome = props.get("buttermilk.datastore.home");
		initDb(dbHome, cachedKey);
		
		
	}

	protected void initDb(String dataHomeDir, SensitiveBytes cachedKey) throws DatabaseException {

		File dbPathFile = new File(dataHomeDir);
		if (!dbPathFile.exists()) {
			dbPathFile.mkdirs();
		}

		db = new ButtermilkDatabase(dataHomeDir, cachedKey);
		views = new ButtermilkViews(db, cachedKey);
	}

	public void closeDb() throws DatabaseException {
		db.close();
		views.clearCachedKey();
	}

	public ButtermilkDatabase getDb() {
		return db;
	}

	public ButtermilkViews getViews() {
		return views;
	}

	public String getRegHandle() {
		return regHandle;
	}

	public Properties getProps() {
		return props;
	}

	public SecurityManagerInterface getSecurityManager() {
		return securityManagerInterface;
	}
	
}
