package com.cryptoregistry.client.deamon;

import java.io.Console;
import java.io.UnsupportedEncodingException;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

import org.jgroups.Address;
import org.jgroups.JChannel;
import org.jgroups.Message;
import org.jgroups.ReceiverAdapter;
import org.jgroups.util.Buffer;
import org.jgroups.util.Util;


public class DataStoreFrontEnd {

	final MODE	 					mode;
	JChannel 						rootChannel;
	BlockingQueue<Message>			queue;
	private static int count = 0;

    protected DataStoreFrontEnd(MODE mode) {
		super();
		this.mode = mode;
		if(mode.equals(MODE.buttermilkDB)){
			queue = new LinkedBlockingQueue<Message>();
		}
	}
    
    protected String processBuf(Message msg) {
    	try {
			return String.valueOf(msg.getObject());
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
    	
    	
    }

	protected void start() throws Exception {
		
		try {
			
	        rootChannel=new JChannel(); // use the default config, udp.xml
	        
	        if(mode == MODE.buttermilkDB){
	        	 rootChannel.setName(mode.name());
	        }else{
	        	  rootChannel.setName(mode.name()+"."+count);
	        	  count++;
	        }
	      
	        rootChannel.setReceiver(new ReceiverAdapter() {
	            public void receive(Message msg) {
	            	//if we are the server, queue the incoming message for processing
	                if(mode == MODE.buttermilkDB){
	                	Address sourceAddress = msg.getSrc();
	                	// if not a message from the DB (ourselves in this case), queue it for processing
	                	if(!sourceAddress.toString().equals(MODE.buttermilkDB.name())){
	                		queue.add(msg);
	                	}
	                	
	                // if we are a client and if the message is from the server, show it; otherwise not
	                }else{
	                	if(msg.getSrc().toString().equals("buttermilkDB")){
	                		System.err.println("\nServer sent: "+processBuf(msg));
	                	}
	                }
	            }
	        });
	        rootChannel.connect("datastore");
	        eventLoop();
	        
		}finally {
			  rootChannel.close();
		}
      
    }
    
    protected void eventLoop() {	
    	
    	if(mode.equals(MODE.buttermilkClient)){
    		
	    	Console con = System.console();
	    	if(con == null) {
	    		throw new RuntimeException("No console available!");
	    	}
	    	
	    	while(true){
	    		try {
	    			String in = con.readLine("%s", "$ ");
	    			if(in == null || in.trim().equals("")) {
	    				con.flush();
	    				continue;
	    			}
				    rootChannel.send(new Message(null, in.trim()));
				    con.flush();
				} catch (Exception e) {
					break;
				}
	    	}
	    	
    	}else{
    		
    		if(mode.equals(MODE.buttermilkDB)){
    	    	while(true){
    	    		try {
    	    			
    	    			// this will block until something comes
    	    			Message val = queue.take();
    	    			
    	    			// message data
    	    			String buf = processBuf(val);
    	    			// do processing of message request
    	    			System.err.println("\nServer got: "+buf);
    	    			
    	    			Message response = new Message(val.getSrc(),"OK");
    	    			// respond
    	    			rootChannel.send(response);
    	    			
    				} catch (Exception e) {
    					break;
    				}
    	    	}
        	}
    	}
    }
    
    public static enum MODE {
    	buttermilkClient,buttermilkDB;
    }

}
