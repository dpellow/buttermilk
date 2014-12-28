package com.cryptoregistry.client.storage;

import java.io.File;

import com.cryptoregistry.client.security.KeyManager;
import com.cryptoregistry.passwords.SensitiveBytes;
import com.sleepycat.je.DatabaseException;

/**
 * Create a BDB-style data store for key data. The store contents will be encrypted using a key loaded by 
 * the keyManager. This type of store is best suited to a server or other single-access, high performance
 * use-case
 * 
 * @author Dave
 * 
 */
public class DataStore {

	protected ButtermilkDatabase db;
	protected ButtermilkViews views;
	protected KeyManager keyManager;
	protected String regHandle;
	
	/**
	 * Pass in a key manager. The SimpleKeyManager relies on BUTTERMILK_HOME to be set
	 * 
	 * key manager will need to scan that home for properties file(s) containing at a minimum:
	 * 
	 * key.filename=symmetric-key.json
	 * buttermilk.datastore.home=C:/path/to/buttermilk-home/db
	 * password=<obfuscated password for the symmetric key>
	 * registration.handle=<our default reg handle>
	 * 
	 * To create a Symmetric Key, use the script in the bin folder of the buttermilk-core distro:
	 * 
	 * bin/symmetric-keygen.cmd 
	 * 
	 * This will make a file called "symmetric-key.json" in the current directory. The contents will
	 * look something like this:
	 * 
	 
{
  "Version" : "Buttermilk Key Materials 1.0",
  "RegHandle" : "my key",
  "Keys" : {
    "08fc870b-e582-450a-9733-7fad7b23698f-S" : {
      "KeyData.Type" : "Symmetric",
      "KeyData.PBEAlgorithm" : "PBKDF2",
      "KeyData.EncryptedData" : "iJUzkLjaeaVLKtl2aLriHN5FPRa_KylFcZ5spK-VKIPNfD09xMcNPJfaTAsMUUk7PZeMPL9F9_9HLgbtIwYCNOeBBFXDdOF_htPOEyx4xSlE50EhT-aWL-XZj40xvK0Mv2BwWaUVNP2Kn7Q1krWglr5f-G5537AiM0Zq_BckDy432F7P9IuYVm93M_hNdNCkT_j2ChydAC76CxUK_NNo80OL_or8EnibfHuIPGGV45Ipj-K-RWEB0263byXq8Q9VP6nV3QZmUsJNXLLkPWfQY3am3j7Ol_fccMV-4rMQKgJYivloZhpE_8uczyD-a8ry5Hski-lV5LbCRzjFE6M36GK9utEDe3LUqc5KpxEg0U_bWTzRL65-s4PTXG1coVWMvzwWg2KiJSI-SH3-GHrG2lN7Pwx4rFR0pGs3t8I9935b4s9tcwg7kR3wefqEENQNiKuYOX_JrrM_hpvpsBRzFxTO8BjsfQkuWJEddzmcEblOoo5nWf5pd0SLAZaYiTVVzYra4mRlZ2-JnahESO3JRahY8z8RM5PONmMYurcvRSySw6FFStuoSqUuYlOdnHNKghTkMP9dJBbBmSeprFzEvwE5tt7iJZmvKlJwfJa6c-z_FDzkOZzhMvkL41eVKVgTKTABGRc8deXNMfDE3NyTUSYBHx1gosiV4Bw5oUETK0lkEIL8GVd9KleqL03yRwejPyHBN-ZaDsXSu6M_GS1aAg==",
      "KeyData.PBESalt" : "bvUXGKDfNbPzIRQOKrNxWF7zqKLBZszOlovJvyCDf8M=",
      "KeyData.Iterations" : "10000"
    }
  }
}
	 * 
	 * 
	 *  * To obfuscate a password use
	 * 
	 * java -cp bracket-properties-1.3.6.jar asia.redact.bracket.properties.Obfuscate <plain text password>
	 * 
	
	
	 * @param props
	 * @param keyManager
	 */
	public DataStore(KeyManager keyManager) {
		this.keyManager = keyManager;
		SensitiveBytes cachedKey = keyManager.loadKey(keyManager.getPassword());
		initDb(keyManager.getDatastoreFolder(), cachedKey);
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

	public KeyManager getSecurityManager() {
		return keyManager;
	}
	
	
}
