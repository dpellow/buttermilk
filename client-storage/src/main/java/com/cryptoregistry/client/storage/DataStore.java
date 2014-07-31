package com.cryptoregistry.client.storage;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import asia.redact.bracket.properties.Properties;
import asia.redact.bracket.properties.mgmt.PropertiesReference;
import asia.redact.bracket.properties.mgmt.ReferenceType;

import com.cryptoregistry.client.security.TwoFactorSecurityManager;
import com.cryptoregistry.passwords.Password;
import com.cryptoregistry.passwords.SensitiveBytes;
import com.sleepycat.je.DatabaseException;

/**
 * Create a datastore for key data. The store will be encrypted using two factor security. to
 * prepare for this you will need a thumb drive (flash drive)
 * 
 * @author Dave
 * 
 */
public class DataStore {

	private static final String DEFAULT_DB_DATABASE_FOLDERNAME = "buttermilk-db";

	protected ButtermilkDatabase db;
	protected ButtermilkViews views;
	protected Properties props;
	protected TwoFactorSecurityManager securityManager;

	/**
	 * Will put the store into <user.home>/buttermilk-db
	 */
	public DataStore(Password password) {
		
		SensitiveBytes cachedKey = null;
		
		initProperties();
		securityManager = new TwoFactorSecurityManager(props);
		if(!securityManager.checkForRemovableDisk()) {
			throw new RuntimeException("Please insert removable disk");
		}
		if(securityManager.keysExist()) {
			cachedKey = securityManager.loadKey(password);
		}else{
			securityManager.generateAndSecureKeys(password);
		}
		
		initDb(defaultDBFolder(), cachedKey);
		
		if(password.isAlive()) password.selfDestruct();
		
	}

	protected void initProperties() {

		String userHome = System.getProperty("user.home");
		String overridePath = userHome + File.separator
				+ "buttermilk.properties";

		List<PropertiesReference> refs = new ArrayList<PropertiesReference>();
		refs.add(new PropertiesReference(ReferenceType.CLASSLOADED,
				"/buttermilk.properties"));
		refs.add(new PropertiesReference(ReferenceType.EXTERNAL, overridePath));

		props = Properties.Factory.loadReferences(refs);
	}

	protected void initDb(String dataHomeDir, SensitiveBytes cachedKey) throws DatabaseException {

		File dbPathFile = new File(dataHomeDir);
		if (!dbPathFile.exists()) {
			dbPathFile.mkdirs();
		}

		db = new ButtermilkDatabase(dataHomeDir, cachedKey);
		views = new ButtermilkViews(db, cachedKey);
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
		views.clearCachedKey();
	}

	public ButtermilkDatabase getDb() {
		return db;
	}

	public ButtermilkViews getViews() {
		return views;
	}
	
	

}
