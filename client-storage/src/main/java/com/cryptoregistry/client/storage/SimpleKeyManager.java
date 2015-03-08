/*
 *  This file is part of Buttermilk(TM) 
 *  Copyright 2014 David R. Smith for cryptoregistry.com
 *
 */
package com.cryptoregistry.client.storage;

import java.io.File;
import java.nio.charset.Charset;
import java.util.Iterator;
import java.util.List;

import asia.redact.bracket.properties.Obfuscate;
import asia.redact.bracket.properties.Properties;

import com.cryptoregistry.CryptoKeyWrapper;
import com.cryptoregistry.KeyMaterials;
import com.cryptoregistry.client.security.KeyManager;
import com.cryptoregistry.formats.JSONReader;
import com.cryptoregistry.passwords.NewPassword;
import com.cryptoregistry.passwords.Password;
import com.cryptoregistry.passwords.SensitiveBytes;
import com.cryptoregistry.symmetric.SymmetricKeyContents;

/**
 *<p>KeyManagers deal with the task of unlocking the Data store contents. This
 *simple one just uses a key stored on the file system and an obfuscated
 *password found in an associated properties file</p>
 *
 * <p>
 * When initialized with the no-arg constructor it looks for configuration 
 * based on a "BUTTERMILK_HOME" environment variable. This should
 * be set to a folder with buttermilk.properties and a symmetric key.
 * </p>
 * 
 * <p>
 * The alternative is to initialize the store location with a path 
 * passed in via the other constructor.
 * </p>
 * 
 * 
 * @author Dave
 *
 */
public class SimpleKeyManager implements KeyManager {

	Properties props;
	String buttermilkHome;
	String path_s; // path to key
	
	public SimpleKeyManager() {
		init();
	}
	
	public SimpleKeyManager(String buttermilkHome) {
		this.buttermilkHome = buttermilkHome;
		init();
	}
	
	private void init(){
		if(buttermilkHome == null) {
			buttermilkHome = System.getenv("BUTTERMILK_HOME");
		}
		
		if(buttermilkHome == null) throw new RuntimeException("Path is null, set BUTTERMILK_HOME in your env variables");
		File f = new File(buttermilkHome);
		
		// suck in any file with .properties in this folder and combine them
		
		props =  Properties.Factory.getInstance();
		if(f.exists() && f.isDirectory()){
			File [] files = f.listFiles();
			for(int i = 0;i<files.length;i++){
				if(!files[i].getPath().endsWith(".properties")) continue;
				if(files[i].isFile()){
					Properties p = null;
					try {
						p = Properties.Factory.getInstance(files[i], Charset.forName("UTF-8"));
					}catch(Exception x){}
					
					if(p != null) props.merge(p);
				}
			}
		}else{
			throw new RuntimeException("BUTTERMILK_HOME does not exist");
		}
		
		path_s = buttermilkHome + File.separatorChar + props.get("key.filename");
	}

	@Override
	public SensitiveBytes loadKey(Password password) {
		SymmetricKeyContents s = null;

		File pf = new File(path_s);
		if (!pf.exists()) {
			throw new RuntimeException(
					"key file p cannot be read or does not exist");
		}
		
		JSONReader reader = new JSONReader(pf);
		KeyMaterials km = reader.parse();
		List<CryptoKeyWrapper> items_p = km.keys();
		Iterator<CryptoKeyWrapper> iter = items_p.iterator();
		while (iter.hasNext()) {
			CryptoKeyWrapper wrapper = iter.next();
			if (wrapper.isSecure()) {
				// p
				boolean ok = wrapper.unlock(password);
				if (ok) {
					s = (SymmetricKeyContents) wrapper.getKeyContents();
				}
			}
		}

		return new SensitiveBytes(s.getBytes());

	}

	@Override
	public Password getPassword() {
		if (!props.containsKey("password")) {
			throw new RuntimeException(
					"password key not found in the password file. Has it been created?");
		}
		String val = props.get("password");
		char[] pass = Obfuscate.FACTORY.decryptToChar(val);
		return new NewPassword(pass);
	}

	@Override
	public boolean keysExist() {

		File pf = new File(path_s);
		if (!pf.exists()) {
			return false;
		}

		return true;
	}

	@Override
	public String getDatastoreFolder() {
		if(!props.containsKey("buttermilk.datastore.home")){
			throw new RuntimeException("Please define buttermilk.datastore.home in your properties");
		}
		String rel = props.get("buttermilk.datastore.home");
		return buttermilkHome+File.separator+rel;
	}

	@Override
	public String toString() {
		return "SimpleKeyManager [buttermilkHome=" + buttermilkHome + "]";
	}
	

	public Properties getProperties() {
		return props;
	}

}
