package com.cryptoregistry.btls;

import java.io.IOException;
import java.io.InputStream;

import com.cryptoregistry.proto.frame.InputFrameReader;

/**
 * Demo client - listen for a message on the server side, print it, and exit. The communication is
 * transparently encrypted over the socket.
 * 
 * @author Dave
 *
 */
public class PrintHandler implements Runnable {

	SecureSocket socket;
	
	public PrintHandler(SecureSocket socket) {
		this.socket = socket;
	}

	@Override
	public void run() {
		
		try {
			InputStream input = socket.getInputStream();
			InputFrameReader reader = new InputFrameReader();
			System.err.println(reader.readStringProto(input));
		} catch (IOException e) {
			e.printStackTrace();
		}finally{
			try {
				if(!socket.isClosed()) {
					socket.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
}
