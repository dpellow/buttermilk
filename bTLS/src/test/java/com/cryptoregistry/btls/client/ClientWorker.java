/*
 *  This file is part of Buttermilk
 *  Copyright 2011-2015 David R. Smith All Rights Reserved.
 *
 */
package com.cryptoregistry.btls.client;

import java.io.IOException;
import java.io.InputStream;
import java.net.Socket;

import javax.swing.JTextArea;

/**
 * Client needs distinct thread to listen for incoming messages on the socket's FrameInputStream
 * 
 * @author Dave
 *
 */
public class ClientWorker implements Runnable {

	private Socket socket;
	private JTextArea textArea;
	
	public ClientWorker(Socket socket, JTextArea textArea) {
		this.socket = socket;
		this.textArea = textArea;
	}

	@Override
	public void run() {
		InputStream in;
		try {
			in = socket.getInputStream();
			byte [] bytes = new byte[1024];
			while(true){
				int count = in.read(bytes);
				if(count == -1) break;
				String msg = new String(bytes,0,count, "UTF-8");
				//System.err.println("Got: "+msg);
				textArea.append(msg+"\n");
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		System.err.println("Exiting IncomingListener thread....");
	}

}
