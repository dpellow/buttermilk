package com.cryptoregistry.btls;

import java.io.IOException;

public class ServerTest {
	
	final static int port = 4444;

	public static void main(String[] args) {
		System.err.println("Starting server...");
		try {
			BTLSServerSocket ss = new BTLSServerSocket(port);
			
			while(true){
				// blocks until a connection is made.
				BTLSSocket socket = (BTLSSocket) ss.accept();
				Thread t = new Thread(new SecureClient(socket));
				t.start();
			}
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		

	}

}
