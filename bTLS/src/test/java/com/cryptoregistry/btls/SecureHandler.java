package com.cryptoregistry.btls;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

/**
 * Demo client - listen for a message on the server side, print it
 * 
 * @author Dave
 *
 */
public class SecureHandler implements Runnable {

	C2Socket socket;
	
	public SecureHandler(C2Socket socket) {
		this.socket = socket;
	}

	@Override
	public void run() {
		
		try {
			SecureMessageInputStream in = (SecureMessageInputStream) socket.getInputStream();
			Object obj = in.readSecureMessage();
			if(obj instanceof String){
				System.out.println(obj);
			}else{
				String s = new String((byte[])obj,StandardCharsets.UTF_8);
				System.out.println(s);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}finally{
			try {
				socket.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		
	}

}
