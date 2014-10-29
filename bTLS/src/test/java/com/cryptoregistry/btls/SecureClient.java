package com.cryptoregistry.btls;

/**
 * Demo client - listen for a message
 * 
 * @author Dave
 *
 */
public class SecureClient implements Runnable {

	BTLSSocket socket;
	
	public SecureClient(BTLSSocket socket) {
		this.socket = socket;
	}

	@Override
	public void run() {
		// construct message and send
		
	}

}
