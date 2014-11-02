package com.cryptoregistry.btls;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PipedInputStream;
import java.io.PipedOutputStream;

import junit.framework.Assert;

import org.junit.Test;

import com.cryptoregistry.c2.key.Curve25519KeyContents;
import com.cryptoregistry.c2.key.Curve25519KeyForPublication;
import com.cryptoregistry.proto.frame.C2KeyForPublicationOutputFrame;
import com.cryptoregistry.proto.frame.InputFrameReader;

public class BTLSSocketTest {

	@Test
	public void test0() {

		Curve25519KeyContents clientKey = Configuration.CONFIG.clientKey();

		PipedOutputStream pout = new PipedOutputStream();
		PipedInputStream pin = new PipedInputStream(16);
		try {
			pout.connect(pin);
			new Thread(new HelloClient(clientKey, pout)).start();
			new HelloServer(pin).run();
			
			
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		Assert.assertTrue(true);
	}
	
	class HelloClient implements Runnable {
		
		OutputStream out;
		Curve25519KeyContents clientKey;
		
		HelloClient(Curve25519KeyContents contents, OutputStream out){
			this.clientKey = contents;
			this.out=out;
		}

		@Override
		public void run() {
			// 1.0 send our client public key
				Curve25519KeyForPublication _public = ((Curve25519KeyContents)clientKey).forPublication();
				C2KeyForPublicationOutputFrame frame = new C2KeyForPublicationOutputFrame(_public);
				frame.writeFrame(out);
				System.err.println("Wrote public portion of key");
		}
	}
	
	class HelloServer implements Runnable {
		
		InputStream in;
		Curve25519KeyForPublication clientKey;
		
		HelloServer(InputStream in){
			this.in=in;
		}

		@Override
		public void run() {
			InputFrameReader reader = new InputFrameReader();
			clientKey = reader.readC2KeyContents(in);
			System.err.println(clientKey.getDistinguishedHandle());
		}
	}		

}
