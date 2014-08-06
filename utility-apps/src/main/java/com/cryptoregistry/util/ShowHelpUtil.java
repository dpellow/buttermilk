package com.cryptoregistry.util;

import java.io.IOException;
import java.io.InputStream;

public class ShowHelpUtil {

	public static final void showHelp(String path) {
		InputStream in = Thread.currentThread().getClass().getResourceAsStream(path);
		try {
	        byte[] buf = new byte[1024];
	        int len;
	        while((len=in.read(buf))>0){
	            System.out.write(buf,0,len);
	        }
	       
	    } catch (Exception e) {
	        e.printStackTrace();
	    }finally{
	    	if(in != null){
	    		 try {
					in.close();
				} catch (IOException e) {}
	    	}
	    }
	}

}
