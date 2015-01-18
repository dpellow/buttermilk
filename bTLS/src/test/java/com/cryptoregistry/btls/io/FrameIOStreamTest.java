package com.cryptoregistry.btls.io;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

import org.junit.Test;

import com.cryptoregistry.btls.BTLSProtocol;
import com.cryptoregistry.symmetric.CryptoFactory;
import com.cryptoregistry.symmetric.SymmetricKeyContents;

public class FrameIOStreamTest implements AlertListener {

	@Test
	public void test0() {
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		SymmetricKeyContents key = CryptoFactory.INSTANCE.generateKey(256);
		byte [] iv = {0x00,0x01,0x02,0x03,0x04,0x05,0x06,0x07,0x08,0x09,0x10,0x11,0x12,0x13,0x14,0x15};
		FrameOutputStream fout = new FrameOutputStream(out,key.getBytes(),iv);
		String hello0 = "Hi there, how are you? I am fine, hope you are well.";
		String hello1 = "\nGreat!";
		String hello2 = "\nNow what?";
		try {
			fout.write(hello0.getBytes("UTF-8"));
			fout.flush();
			
			fout.writeInformationalAlert("Internet bandwidth just slowed down 10%");
			
			fout.write(hello1.getBytes("UTF-8"));
			fout.flush();
			
			fout.write(hello2.getBytes("UTF-8"));
			fout.flush();
			
			fout.writeAlert(BTLSProtocol.EMERGENCY_CLOSE_SOCKET_IMMEDIATELY, "Close socket!");
			
			fout.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		ByteArrayInputStream in = new ByteArrayInputStream(out.toByteArray());
		FrameInputStream fin = new FrameInputStream(in,key.getBytes());
		fin.addAlertListener(this);
		StringBuffer sbuf = new StringBuffer();
		byte [] buf = new byte[10];
		int ct = 0;
		try {
			
			while((ct = fin.read(buf)) != -1) {
				
					byte [] got = new byte[ct];
					System.arraycopy(buf, 0, got, 0, ct);
					sbuf.append(new String(got,"UTF-8"));
			}
		
			fin.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		System.err.println(sbuf.toString());
		
	}

	@Override
	public void alertReceived(AlertEvent evt) {
		System.err.println("Got Alert: "+evt);
	}

}
