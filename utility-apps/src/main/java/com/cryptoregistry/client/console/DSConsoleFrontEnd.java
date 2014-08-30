package com.cryptoregistry.client.console;

import java.io.Console;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

import org.jgroups.Address;
import org.jgroups.JChannel;
import org.jgroups.Message;
import org.jgroups.ReceiverAdapter;

import com.cryptoregistry.client.storage.DataStore;


public class DSConsoleFrontEnd {

	final MODE	 					mode;
	JChannel 						rootChannel;
	BlockingQueue<Message>			queue;
	private static int 				count = 0;
	private boolean 				wait;
	
	private final DataStore				ds;

	// ds can be null for client
    protected DSConsoleFrontEnd(MODE mode, DataStore ds) {
		super();
		this.mode = mode;
		if(mode.equals(MODE.buttermilkDB)){
			queue = new LinkedBlockingQueue<Message>();
			this.ds = ds;
		}else{
			this.ds = null;
		}
	}
    
    protected String processBuf(Message msg) {
    	try {
			return String.valueOf(msg.getObject());
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
    }
    
    protected void initShutdownHook() {
    	Runtime.getRuntime().addShutdownHook(new Thread() {
    	    public void run() { 
    	     if(ds != null) ds.closeDb();
    	     System.err.println("Closed datastore in shutdown hook.");
    	     }
    	 });
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
	                		System.err.println("  "+processBuf(msg));
	                		System.err.flush();
	                		wait = false;
	                	}
	                }
	            }
	        });
	        rootChannel.connect("datastore");
	        eventLoop();
	        
		}finally {
			  rootChannel.close();
			  if(ds != null){
				  ds.closeDb();
				  System.err.println("Closed datastore.");
			  }
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
	    			if(wait) {
	    				Thread.sleep(50);
	    				continue;
	    			}
	    			String in = con.readLine("%s", "$ ");
	    			if(in == null || in.trim().equals("")) {
	    				continue;
	    			}
				    rootChannel.send(new Message(null, in.trim()));
				    wait = true;
				    
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
    	    			System.err.println("Server got: "+buf);
    	    			
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
