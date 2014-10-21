package com.cryptoregistry.client.security;

import java.io.File;
import java.nio.charset.Charset;
import java.util.Iterator;
import java.util.List;

import asia.redact.bracket.properties.Obfuscate;
import asia.redact.bracket.properties.Properties;

import com.cryptoregistry.CryptoKeyWrapper;
import com.cryptoregistry.KeyMaterials;
import com.cryptoregistry.ec.CryptoFactory;
import com.cryptoregistry.ec.ECKeyContents;
import com.cryptoregistry.ec.ECKeyForPublication;
import com.cryptoregistry.formats.JSONReader;
import com.cryptoregistry.passwords.NewPassword;
import com.cryptoregistry.passwords.Password;
import com.cryptoregistry.passwords.SensitiveBytes;

/**
 * Looks for configuration based on a "BUTTERMILK_HOME" environment variable. This should
 * be set to a folder with buttermilk.properties and buttermilk.password files
 * 
 * 
 * @author Dave
 *
 */
public class TrivialSecurityManager implements SecurityManagerInterface {

	Properties props;
	String path_p, path_q;
	
	public TrivialSecurityManager(Properties props) {
		this.props = props;
		init();
		path_p = props.get("p.home") + File.separatorChar + props.get("key.filename");
		path_q = props.get("q.home") + File.separatorChar + props.get("key.filename");
	}
	
	private void init(){
		String path = System.getenv("BUTTERMILK_HOME");
		if(path == null) throw new RuntimeException("Path is null, set BUTTERMILK_HOME in your env");
		File f = new File(path);
		
		// suck in any file in this folder assuming it is properties
		
		if(f.exists() && f.isDirectory()){
			File [] files = f.listFiles();
			for(int i = 0;i<files.length;i++){
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
	}

	@Override
	public SensitiveBytes loadKey(Password password) {
		ECKeyContents p = null;
		ECKeyForPublication _q = null;

		// test if paths exist. If so, then existing encryption may be at risk
		// of being overwritten. Exit.
		File pf = new File(path_p);
		if (!pf.exists()) {
			throw new RuntimeException(
					"key file p cannot be read or does not exist");
		}
		File qf = new File(path_q);
		if (!qf.exists()) {
			throw new RuntimeException(
					"key file q cannot be read or does not exist");
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
					p = (ECKeyContents) wrapper.getKeyContents();
				}
			}
		}

		reader = new JSONReader(qf);
		km = reader.parse();
		List<CryptoKeyWrapper> items_q = km.keys();
		iter = items_q.iterator();
		while (iter.hasNext()) {
			CryptoKeyWrapper wrapper = iter.next();
			if (wrapper.isForPublication()) {
				// _q
				_q = (ECKeyForPublication) wrapper.getKeyContents();
			}
		}

		return new SensitiveBytes(CryptoFactory.INSTANCE.keyAgreement(p, _q));

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

		File pf = new File(path_p);
		if (!pf.exists()) {
			return false;
		}
		File qf = new File(path_q);
		if (!qf.exists()) {
			return false;
		}

		return true;
	}

}
