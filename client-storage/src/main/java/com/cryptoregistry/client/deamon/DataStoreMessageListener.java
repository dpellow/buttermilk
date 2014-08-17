package com.cryptoregistry.client.deamon;

import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.util.concurrent.BlockingQueue;

import org.jgroups.Message;
import org.jgroups.MessageListener;

public class DataStoreMessageListener implements MessageListener {
	
	BlockingQueue<String> 	queue;

	public DataStoreMessageListener(BlockingQueue<String> queue) {
		this.queue = queue;
	}

	@Override
	public void receive(Message msg) {
		try {
			System.err.println("received:"+msg);
			queue.add(new String(msg.getBuffer(),"UTF-8"));
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void getState(OutputStream output) throws Exception {
		// TODO Auto-generated method stub

	}

	@Override
	public void setState(InputStream input) throws Exception {
		// TODO Auto-generated method stub

	}

}
