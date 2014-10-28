package com.cryptoregistry.crypto.mt;

import java.security.SecureRandom;
import java.util.List;
import java.util.concurrent.Future;
import junit.framework.Assert;
import org.junit.Test;


public class AESTest {
	
	long startTime = System.nanoTime();
	long elapsed=0, dif=0;
	
	void t(String desc){
		long lastElapsed = elapsed;
		elapsed = System.nanoTime()-startTime;
		dif = elapsed-lastElapsed;
		System.err.println("("+desc+" - elapsed time:"+elapsed/1000000+"ms, delta time:"+dif/1000000+"ms)");
	}
	
	
	@Test
	public void test3() {
		String s = "abcdefghijklmnopqrstuvwxyz";
		LargeMessage msg = new LargeMessage(s);
		Assert.assertEquals(9, msg.count());
	}
	
	
	@Test
	public void test2() {
		
		SecureRandom rand = new SecureRandom();
		byte [] key = new byte[32];
		byte [] exampleData = new byte[1048576*8]; // 1 megabyte * 8
		
		rand.nextBytes(key);
		rand.nextBytes(exampleData);
		
		startTime = System.nanoTime();
		t("start");
		
		LargeMessage msg = new LargeMessage(exampleData);
		LargeMessageService service = new LargeMessageService(key,msg);
		service.encrypt();
	
		t("encrypt complete");
		
		msg.rotate();
		
		service = new LargeMessageService(key,msg);
		
		service.decrypt();
		
		t("decrypt complete");
		
		Assert.assertTrue(test_equal(exampleData,msg.byteResult()));
	}
	
	@Test
	public void test1a() {
		t("start");
	
		SecureRandom rand = new SecureRandom();
		byte [] key = new byte[32];
		byte [] iv = new byte[16];
		byte [] exampleData = new byte[1048576*8]; // a megabyte *8
		rand.nextBytes(key);
		rand.nextBytes(iv);
		rand.nextBytes(exampleData);
		
		t("fill rand data");
		
		AESGCM_MT aes = new AESGCM_MT(key,iv);
	
		t("init");
		
		byte [] encrypted = aes.encrypt(exampleData);
		
		t("encrypt1");
		
	//	byte [] unencrypted = aes.decrypt(encrypted);
		
	//	t("unencrypt1");
		
	//	Assert.assertTrue(test_equal(exampleData,unencrypted));
		
	}
	
	
	@Test
	public void test1() {
		t("start");
	
		SecureRandom rand = new SecureRandom();
		byte [] key = new byte[32];
		byte [] iv = new byte[16];
		byte [] exampleData = new byte[1048576]; // a megabyte
		rand.nextBytes(key);
		rand.nextBytes(iv);
		rand.nextBytes(exampleData);
		
		
		t("fill rand data");
		
		AESGCM_MT aes = new AESGCM_MT(key,iv);
		AESGCM_MT aes2 = new AESGCM_MT(key,iv);
		AESGCM_MT aes3 = new AESGCM_MT(key,iv);
	
		t("init");
		
		byte [] encrypted = aes.encrypt(exampleData);
		
		t("encrypt1");
		
		byte [] encrypted2 = aes2.encrypt(exampleData);
		
		t("encrypt2");
		
		byte [] encrypted3 = aes3.encrypt(exampleData);
		
		t("encrypt3");
		
		byte [] unencrypted = aes.decrypt(encrypted);
		
		t("unencrypt1");
		
		byte [] unencrypted2 = aes2.decrypt(encrypted2);
	
		t("unencrypt2");
		
		byte [] unencrypted3 = aes3.decrypt(encrypted3);
		
		t("unencrypt3");
		
		Assert.assertTrue(test_equal(exampleData,unencrypted));
		Assert.assertTrue(test_equal(exampleData,unencrypted2));
		Assert.assertTrue(test_equal(exampleData,unencrypted3));
	}
	
	private boolean test_equal(byte[] a, byte[] b) {
		
		if(a.length != b.length) return false;
		
		int i;
		for (i = 0; i < a.length; i++) {
			if (a[i] != b[i]) {
				return false;
			}
		}
		return true;
	}

}
