package com.cryptoregistry.client.console;

public class DSConsoleClient extends DSConsoleFrontEnd {

	public DSConsoleClient(MODE name) {
		super(name, null);
	}
	
	public static void main(String [] args){
		
		try {
			new DSConsoleClient(MODE.buttermilkClient).start();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}


}
