package com.cryptoregistry.util;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.StringWriter;

/**
 * Helpful routines for presenting command-line output
 *  
 * @author Dave
 *
 */
public class ShowHelpUtil {

	/**
	 * Send a help file to Standard out
	 * 
	 * @param path
	 */
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
	
	/**
	 * Return the help file contents as a String
	 * 
	 * @param path
	 * @return
	 */
	public static final String help(String path) {
		InputStream in = Thread.currentThread().getClass().getResourceAsStream(path);
		InputStreamReader reader = new InputStreamReader(in);
		StringWriter builder = new StringWriter();
		try {
	        char[] buf = new char[1024];
	        int len;
	        while((len=reader.read(buf))>0){
	            builder.write(buf,0,len);
	        }
	       
	    } catch (Exception e) {
	        e.printStackTrace();
	    }finally{
	    	if(reader != null){
	    		 try {
					reader.close();
				} catch (IOException e) {}
	    	}
	    }
		return builder.toString();
	}
	
	/**
	 * Return a formatted response
	 * 
	 * @param path
	 * @return
	 */
	public static final String formatResponse(String cmdLine, String response, long ms) {
		InputStream in = Thread.currentThread().getClass().getResourceAsStream("/response.txt");
		InputStreamReader reader = new InputStreamReader(in);
		StringWriter builder = new StringWriter();
		try {
	        char[] buf = new char[1024];
	        int len;
	        while((len=reader.read(buf))>0){
	            builder.write(buf,0,len);
	        }
	       
	    } catch (Exception e) {
	        e.printStackTrace();
	    }finally{
	    	if(reader != null){
	    		 try {
					reader.close();
				} catch (IOException e) {}
	    	}
	    }
		return String.format(builder.toString(), cmdLine,response,ms);
	}

}
