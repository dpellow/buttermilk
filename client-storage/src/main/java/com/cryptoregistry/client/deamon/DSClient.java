package com.cryptoregistry.client.deamon;

public class DSClient extends DataStoreFrontEnd {

	public DSClient(MODE name) {
		super(name);
	}
	
	public static void main(String [] args){
		
		try {
			new DSClient(MODE.buttermilkClient).start();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}


}
