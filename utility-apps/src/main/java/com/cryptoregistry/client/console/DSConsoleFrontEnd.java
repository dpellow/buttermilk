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

	final MODE	 						mode;
	JChannel 							rootChannel;
	BlockingQueue<Message>				queue;
	private static int 					count = 0;
	
	protected final DataStore			ds;
	

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
		
		initShutdownHook();
		
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
	                		System.out.println("  "+processBuf(msg));
	                		System.out.flush();
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
				  System.err.println("Closed datastore. Done.");
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
	    				String in = con.readLine("%s", "$ ");
	    				if(in == null || in.trim().equals("")) continue;
	    				if(in.contains("quit") || in.contains("exit")){
	    					System.out.println("Exiting");
	    					break;
	    				}
	    				rootChannel.send(new Message(null, in.trim()));
				} catch (Exception e) {
					e.printStackTrace();
					break;
				}
	    	}
	    	
    	}else{
    		
    		if(mode.equals(MODE.buttermilkDB)){
    			
    			System.out.println("Starting server event loop...");
    	    	while(true){
    	    		try {
    	    			
    	    			// this will block until something comes
    	    			Message val = queue.take();
    	    			
    	    			// message data
    	    			String buf = processBuf(val);
    	    			// do processing of message request
    	    			System.err.println("Server got: "+buf);
    	    			
    	    			ButtermilkExec app = new ButtermilkExec(ds);
    	    			app.init(args(buf));
    	    			String res = app.execute();
    	    			
    	    			Message response = new Message(val.getSrc(),res);
    	    			// respond
    	    			rootChannel.send(response);
    	    			
    	    			if(res.equals("DONE")){
    	    				break;
    	    			}
    	    			
    				} catch (Exception e) {
    					break;
    				}
    	    	}
        	}
    	}
    }
    
    private String [] args(String s){
    	String [] args = s.split(" ");
    	if(args.length == 0) {
    		args = new String[1];
    		args[0] =s;
    		return args;
    	}else{
    		return args;
    	}
    }
    
    
    public static enum MODE {
    	buttermilkClient,buttermilkDB;
    }

}
