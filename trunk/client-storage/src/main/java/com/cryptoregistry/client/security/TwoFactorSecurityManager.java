package com.cryptoregistry.client.security;

import java.io.File;
import java.io.IOException;
import java.io.StringWriter;
import java.nio.charset.Charset;
import java.nio.file.FileStore;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Iterator;
import java.util.List;

import asia.redact.bracket.properties.Properties;

import com.cryptoregistry.CryptoKeyWrapper;
import com.cryptoregistry.KeyMaterials;
import com.cryptoregistry.ec.CryptoFactory;
import com.cryptoregistry.ec.ECKeyContents;
import com.cryptoregistry.ec.ECKeyForPublication;
import com.cryptoregistry.formats.JSONBuilder;
import com.cryptoregistry.formats.JSONReader;
import com.cryptoregistry.passwords.Password;
import com.cryptoregistry.passwords.SensitiveBytes;

/**
 * Cheap two-factor security: a thumb drive and two EC keys. The thumb drive can be removed 
 * from the PC once the program has initialized.
 * 
 * @author Dave
 *
 */
public class TwoFactorSecurityManager {

	String location_p, location_q, fileName;
	String curveName;
	String registrationHandle;
	
	public TwoFactorSecurityManager(Properties props) {
		curveName = props.get("curve.name");
		location_p = props.get("p.home");
		location_q = props.get("q.home");
		fileName = props.get("key.filename");
		registrationHandle = props.get("registration.handle");
	}
	
	public SensitiveBytes loadKey(Password password) {
		
		ECKeyContents p = null;
		ECKeyForPublication _q = null;
		
		String path_p = location_p+File.separatorChar+fileName;
		String path_q = location_q+File.separatorChar+fileName;
		
		// test if paths exist. If so, then existing encryption may be at risk of being overwritten. Exit.
		File pf= new File(path_p);
		if(!pf.exists()) {
			throw new RuntimeException(
					"key file p cannot be read or does not exist"
				);
		}
		File qf= new File(path_q);
		if(!qf.exists()) {
			throw new RuntimeException(
					"key file q cannot be read or does not exist"
				);
		}
		
		JSONReader reader = new JSONReader(pf);
		KeyMaterials km = reader.parse();
		List<CryptoKeyWrapper> items_p = km.keys();
		Iterator<CryptoKeyWrapper> iter = items_p.iterator();
		while(iter.hasNext()) {
			CryptoKeyWrapper wrapper = iter.next();
			if(wrapper.isSecure()){
				// p
				boolean ok = wrapper.unlock(password);
				if(ok) {
					p = (ECKeyContents) wrapper.getKeyContents();
				}
			}
		}
		
		reader = new JSONReader(qf);
		km = reader.parse();
		List<CryptoKeyWrapper> items_q = km.keys();
		iter = items_q.iterator();
		while(iter.hasNext()) {
			CryptoKeyWrapper wrapper = iter.next();
			if(wrapper.isForPublication()){
				// _q
				_q = (ECKeyForPublication) wrapper.getKeyContents();
			}
		}
		
		return new SensitiveBytes(CryptoFactory.INSTANCE.keyAgreement(p, _q));
		
	}
	
	public boolean keysExist() {
		
		String path_p = location_p+File.separatorChar+fileName;
		String path_q = location_q+File.separatorChar+fileName;
		
		File pf= new File(path_p);
		if(!pf.exists()) {
			return false;
		}
		File qf= new File(path_q);
		if(!qf.exists()) {
			return false;
		}
		
		return true;
		
	}
	
	public void generateAndSecureKeys(Password password) {
		
		ECKeyContents p, q;
		
		String path_p = location_p+File.separatorChar+fileName;
		String path_q = location_q+File.separatorChar+fileName;
		
		// test if paths exist. If so, then existing encryption may be at risk. Exit.
		File pf= new File(path_p);
		if(pf.exists()) {
			throw new RuntimeException(
					"key file p exists. You must manually remove existing encryption keys to run the method."
				);
		}
		File qf= new File(path_q);
		if(qf.exists()) {
			throw new RuntimeException(
					"key file q exists. You must manually remove existing encryption keys to run the method."
				);
		}
		
		File loc_p = new File(location_p);
		loc_p.mkdirs();
		
		File loc_q = new File(location_q);
		loc_q.mkdirs();
		
		p = CryptoFactory.INSTANCE.generateKeys(password.getPassword(), curveName);
		q = CryptoFactory.INSTANCE.generateKeys(curveName);
		
		JSONBuilder builder = new JSONBuilder(registrationHandle);
		builder.add(p);
		
		StringWriter writer = new StringWriter();
		builder.format(writer);
		try {
			Files.write(Paths.get(path_p), writer.toString().getBytes(Charset.forName("UTF-8")));
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		// only keep _q
		builder = new JSONBuilder(registrationHandle);
		builder.add(q.forPublication());
		
		writer = new StringWriter();
		builder.format(writer);
		try {
			Files.write(Paths.get(path_q), writer.toString().getBytes(Charset.forName("UTF-8")));
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		if(password.isAlive()) password.selfDestruct();
		p.scrubPassword();
		q.scrubPassword();
	}
	
	/**
	 * Return true if a removable disk is inserted. Bit of a kludge...
	 * 
	 * @return
	 */
	public boolean checkForRemovableDisk(){
		 for(Path p:FileSystems.getDefault().getRootDirectories()){
			Iterable<FileStore> stores = p.getFileSystem().getFileStores();
			Iterator<FileStore> iter = stores.iterator();
				while(iter.hasNext()){
					FileStore fs = iter.next();
					if(fs.toString().contains("Removable")) return true;
				}
		 	}
		 
		 return false;
	}
}
