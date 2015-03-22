package com.cryptoregistry.mbean;

import java.lang.management.*; 

import javax.management.*; 

import org.apache.log4j.Logger;
 
public class ButtermilkJMXAgent { 
	
	 static Logger log = Logger.getLogger("com.cryptoregistry.mbean.JMXAgentMain"); 
	 
    public static void main(String[] args) {
    	
    	 try {
    	        MBeanServer mbs = ManagementFactory.getPlatformMBeanServer(); 
    	        ObjectName name = new ObjectName("com.cryptoregistry.mbean:type=DatastoreManager"); 
    	        DatastoreManager mbean = new DatastoreManager(); 
    	        mbs.registerMBean(mbean, name); 
    	    
    	        log.info("Ready.");
    	        Thread.sleep(Long.MAX_VALUE); 
    	    }catch(Exception x){
    	    	x.printStackTrace();
    	    } 
     
   
	}
} 