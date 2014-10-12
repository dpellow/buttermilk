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
		System.err.println("("+desc+" - e:"+elapsed/1000000+", d:"+dif/1000000+") ms");
	}
	
	
	@Test
	public void test3() {
		String s = "abcdefghijklmnopqrstuvwxyz";
		List<Segment> list = Segment.createSegments(s);
		for(Segment seg: list){
			System.err.println((new String(seg.input)));
		}
		Assert.assertEquals(9, list.size());
	}
	
	
	@Test
	public void test2() {
		
		SecureRandom rand = new SecureRandom();
		byte [] key = new byte[32];
		byte [] iv = new byte[16];
		byte [] exampleData = new byte[1048576*8]; // a megabyte * 8
		
		rand.nextBytes(key);
		rand.nextBytes(iv);
		rand.nextBytes(exampleData);
		
		startTime = System.nanoTime();
		t("start");
		
		AESService serv = new AESService(key, iv);
		List<Segment> list = Segment.createSegments(exampleData);
		
		t("created segments");
		
		List<Future<Segment>> futures = serv.runEncryptTasks(list);
		
		t("complete encrypt");
		
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
		int i;
		for (i = 0; i < 32; i++) {
			if (a[i] != b[i]) {
				return false;
			}
		}
		return true;
	}

}
