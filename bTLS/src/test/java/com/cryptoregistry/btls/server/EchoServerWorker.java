package com.cryptoregistry.btls.server;

import java.io.IOException;
import java.io.InputStream;
import java.net.Socket;

/**
 * Worker for the multithreaded echo server
 * 
 * @author Dave
 *
 */
public class EchoServerWorker implements Runnable {

	Socket socket;
	InputStream in = null;

	public EchoServerWorker(Socket socket) {
		super();
		this.socket = socket;
	}

	@Override
	public void run() {
		try {
			in = socket.getInputStream();
			byte[] bytes = new byte[1024];
			while (true) {
				int count = in.read(bytes);
				if(count == -1) break;
				String msg = new String(bytes, 0, count, "UTF-8");
				System.err.print(".");
				socket.getOutputStream().write(msg.getBytes());
				socket.getOutputStream().flush();
			}
		} catch (Exception x) {
			x.printStackTrace();
		}finally{
			if(!socket.isClosed()){
				try {
					socket.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}
}
