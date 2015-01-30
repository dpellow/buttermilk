package com.cryptoregistry.btls;

import java.net.Socket;

public class AlertSender {

	final SecureSocketWrapper wrapper;
	public AlertSender(Socket socket) {
		this.wrapper = (SecureSocketWrapper) socket;
	}
	
	public void send(String msg) {
		wrapper.writeInformationalAlert(msg);
	}
	
	public void send(int code, String msg) {
		wrapper.writeAlert(code,msg);
	}

}
