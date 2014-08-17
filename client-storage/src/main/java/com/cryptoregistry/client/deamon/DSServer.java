package com.cryptoregistry.client.deamon;

public class DSServer extends DataStoreFrontEnd {

	public DSServer(MODE name) {
		super(name);
	}
	
	public static void main(String [] args){
			
		try {
			new DSServer(MODE.buttermilkDB).start();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}


}
