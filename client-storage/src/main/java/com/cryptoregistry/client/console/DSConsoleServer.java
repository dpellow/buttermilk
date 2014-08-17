package com.cryptoregistry.client.console;

public class DSConsoleServer extends DSConsoleFrontEnd {

	public DSConsoleServer(MODE name) {
		super(name);
	}
	
	public static void main(String [] args){
			
		try {
			new DSConsoleServer(MODE.buttermilkDB).start();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}


}
