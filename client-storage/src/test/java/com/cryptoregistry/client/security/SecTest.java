package com.cryptoregistry.client.security;

import java.io.File;
import java.io.InputStream;
import java.util.Arrays;

import junit.framework.Assert;

import org.junit.Test;

import com.cryptoregistry.passwords.NewPassword;
import com.cryptoregistry.passwords.SensitiveBytes;

import asia.redact.bracket.properties.Properties;

public class SecTest {

	@Test
	public void test() {
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
		
		try {
		gen.generateAndSecureKeys("password1".toCharArray());
		}catch(RuntimeException x){
			x.printStackTrace();
		}
		
		if(!pf.exists()) Assert.fail();
		if(!qf.exists()) Assert.fail();
		
		SensitiveBytes sb = gen.loadKey(new NewPassword("password1".toCharArray()));
		System.err.println(Arrays.toString(sb.getData()));
		
	}

}
