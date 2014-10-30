package com.cryptoregistry.btls;

import java.io.IOException;
import java.net.UnknownHostException;

public class ClientTest {

	public static void main(String[] args) {
	
		BTLSSocket socket=null;
		try {
			socket = new BTLSSocket("0.0.0.0", 4444);
			boolean OK = socket.clientsHandshake();
			if(OK){
				SecureMessageOutputStream out = (SecureMessageOutputStream) socket.getOutputStream();
				out.submit("This is a test - Hi There!");
				System.err.println("Sent message...");
				  try {
					Thread.sleep(2000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}else{
				System.err.println("Failed handshake");
			}
		} catch (UnknownHostException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}finally{
			if(socket != null)
				try {
					socket.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
		}

	}

}
