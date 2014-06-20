/*
 *  This file is part of Buttermilk(TM) 
 *  Copyright 2013 David R. Smith for cryptoregistry.com
 *
 */
package com.cryptoregistry.util;

import java.net.*;
import java.io.*;

/**
 * Trivial URL access helper class. A real implementation would use Apache HTTPClient, etc.
 * 
 * @author Dave
 *
 */
class URLGrabber {
	
	String urlString;
	StringBuffer output;
	private static String LS = System.getProperty("line.separator");
	
	public URLGrabber(String urlStr) {
		urlString = urlStr;
		output = new StringBuffer();
	}

	public String grab() {
		try {
			URL url = new URL(urlString);
			
		    URLConnection con = url.openConnection();
		    BufferedReader in = new BufferedReader( new InputStreamReader(con.getInputStream(), "UTF-8"));
		    String inputLine=null;
	
		    while ((inputLine = in.readLine()) != null) {
		            output.append(inputLine);
		            output.append(LS);
		    }
	        in.close();
	        
		} catch (IOException e) {
			e.printStackTrace();
			throw new RuntimeException("URLGrabber failed...", e);
		}
		
		return output.toString();
	}
}
