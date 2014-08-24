package com.cryptoregistry.client.storage;

import java.io.File;
import java.util.List;

import asia.redact.bracket.properties.Properties;
import asia.redact.bracket.properties.mgmt.PropertiesReference;

import com.cryptoregistry.client.security.TwoFactorSecurityManager;
import com.cryptoregistry.passwords.Password;
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
	protected TwoFactorSecurityManager securityManager;
	protected String regHandle;
	
	
	public DataStore(List<PropertiesReference> refList, Password password) {
		
		SensitiveBytes cachedKey = null;
		
		initProperties(refList);
		securityManager = new TwoFactorSecurityManager(props);
		if(!securityManager.checkForRemovableDisk()) {
			throw new RuntimeException("Please insert your Removable Drive");
		}
		if(securityManager.keysExist()) {
			cachedKey = securityManager.loadKey(password);
		}else{
			throw new RuntimeException("Please use twofactor.sh to establish security keys.");
		}
		
		if(!props.containsKey("buttermilk.datastore.home")){
			throw new RuntimeException("Please define buttermilk.datastore.home in your properties");
		}
		String dbHome = props.get("buttermilk.datastore.home");
		initDb(dbHome, cachedKey);
		
		if(password.isAlive()) password.selfDestruct();
		
	}

	protected void initProperties(List<PropertiesReference> refList) {
		
	//  e.g.
	//	List<PropertiesReference> refs = new ArrayList<PropertiesReference>();
	//	refs.add(new PropertiesReference(ReferenceType.CLASSLOADED, "/buttermilk.properties"));
	//	refs.add(new PropertiesReference(ReferenceType.EXTERNAL, overridePath));

		props = Properties.Factory.loadReferences(refList);
		regHandle = props.get("registration.handle");
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

	public TwoFactorSecurityManager getSecurityManager() {
		return securityManager;
	}
	
}
