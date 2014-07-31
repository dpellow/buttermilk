package com.cryptoregistry.client.security;

import java.io.File;
import java.io.InputStream;
import java.io.StringWriter;
import java.util.Arrays;
import java.util.Iterator;

import junit.framework.Assert;

import org.junit.Test;

import com.cryptoregistry.client.storage.ButtermilkViews;
import com.cryptoregistry.client.storage.DataStore;
import com.cryptoregistry.client.storage.Handle;
import com.cryptoregistry.formats.JSONFormatter;
import com.cryptoregistry.passwords.NewPassword;
import com.cryptoregistry.passwords.Password;
import com.cryptoregistry.passwords.SensitiveBytes;
import com.cryptoregistry.rsa.CryptoFactory;
import com.cryptoregistry.rsa.RSAKeyContents;
import com.google.protobuf.InvalidProtocolBufferException;

import asia.redact.bracket.properties.Properties;

public class SecTest {
	
	@Test
	public void test0() {
		InputStream in = Thread.currentThread().getClass().getResourceAsStream("/buttermilk.properties");
		Properties props = Properties.Factory.getInstance(in);
		
		String location_p = props.get("p.home");
		String location_q = props.get("q.home");
		String fileName = props.get("key.filename");
		
		String path_p = location_p+File.separatorChar+fileName;
		String path_q = location_q+File.separatorChar+fileName;
		
		File pf= new File(path_p);
		File qf= new File(path_q);
		
		// clean out - caution, can impact a system
		// pf.delete();
		// qf.delete();
		
		TwoFactorSecurityManager gen = new TwoFactorSecurityManager(props);
		if(!gen.checkForRemovableDisk()){
			Assert.fail();
		}
		
		char [] pass0 = {'p','a','s','s'};
		try {
		gen.generateAndSecureKeys(new NewPassword(pass0));
		}catch(RuntimeException x){
			x.printStackTrace();
		}
		
		if(!pf.exists()) Assert.fail();
		if(!qf.exists()) Assert.fail();
		
		char [] pass1 = {'p','a','s','s'};
		SensitiveBytes sb = gen.loadKey(new NewPassword(pass1));
		System.err.println(Arrays.toString(sb.getData()));
		
	}
	
	@Test
	public void test1() {
		char [] pass0 = {'p','a','s','s'};
		Password password = new NewPassword(pass0);
		DataStore ds = null;
		
		try {
			
			ds = new DataStore(password);
			ButtermilkViews views = ds.getViews();
			
			RSAKeyContents rsa = CryptoFactory.INSTANCE.generateKeys();
			views.put(rsa);
			
			JSONFormatter formatter = new JSONFormatter("Chinese Eyes");
			Iterator<Handle> iter = views.getSecureMap().keySet().iterator();
			while(iter.hasNext()) {
				Handle key = iter.next();
			//	System.err.println(key);
				Object obj;
				try {
					obj = ds.getViews().getSecure(key.getHandle());
					formatter.add((RSAKeyContents)obj);
				} catch (InvalidProtocolBufferException e) {
					e.printStackTrace();
				}
			}
			
			StringWriter writer = new StringWriter();
			formatter.format(writer);
			System.err.println(writer.toString());
	
		}finally {
			if(ds != null) ds.getDb().close();
		}
	}



}
