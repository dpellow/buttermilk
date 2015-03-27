package com.cryptoregistry.app;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

import asia.redact.bracket.properties.Properties;

/**
 * Call the check handle interface - returns 'Y" or 'N' on a 200 response
 * 
 * @author Dave
 *
 */

public class RegHandleChecker {

	Properties props;
	CloseableHttpClient httpclient;
	
	public RegHandleChecker(Properties props) {
		this.props = props;
	}
	
	private URI url(String handle) throws URISyntaxException {
		URIBuilder builder = new URIBuilder();
		 builder.setScheme(props.get("registration.checkhandle.scheme"))
	        .setHost(props.get("registration.checkhandle.hostname"))
	        .setPath(props.get("registration.checkhandle.path"))
	        .setPort(props.intValue("registration.checkhandle.port"))
	        .setParameter("h", handle);
		return builder.build();
	}
	
	public boolean check(String regHandle) {
		try {
		 httpclient = HttpClients.createDefault();
	        try {
	        	
	        	
	            HttpGet httpget = new HttpGet(url(regHandle));

	       //     System.out.println("Executing request " + httpget.getRequestLine());

	            // Create a custom response handler
	            ResponseHandler<String> responseHandler = new ResponseHandler<String>() {

	                public String handleResponse(final HttpResponse response) throws ClientProtocolException, IOException {
	                    int status = response.getStatusLine().getStatusCode();
	                    if (status >= 200 && status < 300) {
	                        HttpEntity entity = response.getEntity();
	                        return entity != null ? EntityUtils.toString(entity) : null;
	                    } else {
	                        throw new ClientProtocolException("Unexpected response status: " + status);
	                    }
	                }

	            };
	            
	           String responseBody = httpclient.execute(httpget, responseHandler);
	           // check if handle is registered (exists). If it is not registered, we assume it is available 
	           if(responseBody.contains("N")) return true;
	            
	        }catch(IOException x){
	        	x.printStackTrace();
	        } catch (URISyntaxException e) {
				e.printStackTrace();
			}
	      }finally {
	    	  try {
	            httpclient.close();
	    	  }catch(Exception z){}
	      }
		
		return false;
	}

}
