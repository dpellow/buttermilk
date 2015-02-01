/*
 *  This file is part of Buttermilk
 *  Copyright 2011-2014 David R. Smith All Rights Reserved.
 *
 */
package com.cryptoregistry.btls.nio;

import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.nio.channels.spi.SelectorProvider;
import java.util.*;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.cryptoregistry.btls.SecureSocketBuilder;
import com.cryptoregistry.client.security.Datastore;

/**
 * This is the Rox tutorial code by James Greenfield
 * 
 * original source: http://rox-xmlrpc.sourceforge.net/niotut/#The code
 * 
 * @author Dave
 */

public class NIOClient implements Runnable {
	// The host:port combination to connect to
	private InetAddress hostAddress;
	private int port;

	// The selector we'll be monitoring
	private Selector selector;

	// The buffer into which we'll read data when it's available
	private ByteBuffer readBuffer = ByteBuffer.allocate(8192);

	// A list of PendingChange instances
	private List<ChangeRequest> pendingChanges = new LinkedList<ChangeRequest>();

	// Maps a SocketChannel to a list of ByteBuffer instances
	private Map<SocketChannel, List<ByteBuffer>> pendingData = new HashMap<SocketChannel, List<ByteBuffer>>();
	
	// Maps a SocketChannel to a ResponseHandler
	private Map<SocketChannel, ResponseHandler> rspHandlers = Collections.synchronizedMap(new HashMap<SocketChannel, ResponseHandler>());
	
	static final Logger logger = LogManager.getLogger(NIOClient.class.getName());
	
	final Datastore ds;
	
	public NIOClient(Datastore ds, InetAddress hostAddress, int port) throws IOException {
		this.ds = ds;
		this.hostAddress = hostAddress;
		this.port = port;
		this.selector = initSelector();
		logger.trace("initialized NIOClient:"+this);
	}

	public void send(byte[] data, ResponseHandler handler) throws IOException {
		logger.trace("entering send with handler: "+handler);
		// Start a new connection
		SocketChannel socket = initiateConnection();
		
		// Register the response handler
		rspHandlers.put(socket, handler);
		
		// And queue the data we want written
		synchronized (pendingData) {
			List<ByteBuffer> queue = pendingData.get(socket);
			if (queue == null) {
				queue = new ArrayList<ByteBuffer>();
				pendingData.put(socket, queue);
			}
			queue.add(ByteBuffer.wrap(data));
		}

		// Finally, wake up our selecting thread so it can make the required changes
		selector.wakeup();
	}

	public void run() {
		logger.trace("entering run()");
		while (true) {
			try {
				// Process any pending changes
				synchronized (pendingChanges) {
					Iterator<ChangeRequest> changes = pendingChanges.iterator();
					while (changes.hasNext()) {
						ChangeRequest change = changes.next();
						switch (change.type) {
							case ChangeRequest.CHANGEOPS:
								logger.trace("case CHANGEOPS");
								SelectionKey key = change.socket.keyFor(selector);
								key.interestOps(change.ops);
								break;
							case ChangeRequest.REGISTER:
								logger.trace("case REGISTER");
								change.socket.register(selector, change.ops);
								break;
							}
						}
					pendingChanges.clear();
				}

				logger.trace("starting to block on select(), waiting for an event");
				// Wait for an event on one of the registered channels
				selector.select();
				logger.trace("select() returned, got an event");

				// Iterate over the set of keys for which events are available
				Iterator<?> selectedKeys = selector.selectedKeys().iterator();
				while (selectedKeys.hasNext()) {
					SelectionKey key = (SelectionKey) selectedKeys.next();
					selectedKeys.remove();

					if (!key.isValid()) {
						continue;
					}
					
					logger.trace("selected: "+key);

					// Check what event is available and deal with it
					if (key.isConnectable()) {
						finishConnection(key);
						logger.trace("finished connection: "+key);
					} else if (key.isReadable()) {
						logger.trace("reading key: "+key);
						read(key);
					} else if (key.isWritable()) {
						logger.trace("writing key: "+key);
						write(key);
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	private void read(SelectionKey key) throws IOException {
		
		SocketChannel socketChannel = (SocketChannel) key.channel();

		// Clear out our read buffer so it's ready for new data
		readBuffer.clear();

		// Attempt to read off the channel
		int numRead = 0;
		try {
			numRead = socketChannel.read(this.readBuffer);
		} catch (IOException e) {
			// The remote forcibly closed the connection, cancel
			// the selection key and close the channel.
			logger.trace("cancelling: "+key);
			key.cancel();
			logger.trace("closing channel for: "+key);
			socketChannel.close();
			return;
		}

		if (numRead == -1) {
			// Remote entity shut the socket down cleanly. Do the
			// same from our end and cancel the channel.
			logger.trace("closing channel and cancelling for: "+key);
			key.channel().close();
			key.cancel();
			return;
		}

		logger.trace("handling response for: "+key);
		// Handle the response
		this.handleResponse(socketChannel, this.readBuffer.array(), numRead);
	}

	private void handleResponse(SocketChannel socketChannel, byte[] data, int numRead) throws IOException {
		// Make a correctly sized copy of the data before handing it
		// to the client
		byte[] rspData = new byte[numRead];
		System.arraycopy(data, 0, rspData, 0, numRead);
		
		// Look up the handler for this channel
		ResponseHandler handler = this.rspHandlers.get(socketChannel);
		
		// And pass the response to it
		if (handler.handleResponse(rspData)) {
			// The handler has seen enough, close the connection
			socketChannel.close();
			socketChannel.keyFor(selector).cancel();
		}
	}

	private void write(SelectionKey key) throws IOException {
		
		logger.trace("entering write(): "+key);
		SocketChannel socketChannel = (SocketChannel) key.channel();

		synchronized (pendingData) {
			List<?> queue = pendingData.get(socketChannel);

			// Write until there's not more data ...
			while (!queue.isEmpty()) {
				logger.trace("writing data for: "+key);
				ByteBuffer buf = (ByteBuffer) queue.get(0);
				socketChannel.write(buf);
				if (buf.remaining() > 0) {
					// ... or the socket's buffer fills up
					break;
				}
				queue.remove(0);
			}

			logger.trace("going back to waiting to read for: "+key);
			if (queue.isEmpty()) {
				// We wrote away all data, so we're no longer interested
				// in writing on this socket. Switch back to waiting for
				// data.
				key.interestOps(SelectionKey.OP_READ);
			}
		}
	}

	private void finishConnection(SelectionKey key) throws IOException {
		
		logger.trace("finishing connect for: "+key);
		SocketChannel socketChannel = (SocketChannel) key.channel();
	
		// Finish the connection. If the connection operation failed
		// this will raise an IOException.
		try {
			socketChannel.finishConnect();
		} catch (IOException e) {
			// Cancel the channel's registration with our selector
			e.printStackTrace();
			key.cancel();
			return;
		}
	
		logger.trace("registering an interest in writing to: "+key);
		// Register an interest in writing on this channel
		key.interestOps(SelectionKey.OP_WRITE);
	}

	private SocketChannel initiateConnection() throws IOException {
		logger.trace("initiating connection");
		// Create a non-blocking socket channel
		SocketChannel socketChannel = SocketChannel.open();
		socketChannel.configureBlocking(false);
		logger.trace("calling connect()");
		// Kick off connection establishment
		socketChannel.connect(new InetSocketAddress(hostAddress, port));
	
		// Queue a channel registration since the caller is not the 
		// selecting thread. As part of the registration we'll register
		// an interest in connection events. These are raised when a channel
		// is ready to complete connection establishment.
		synchronized(pendingChanges) {
			logger.trace("adding pending change for OPT_CONNECT");
			pendingChanges.add(new ChangeRequest(socketChannel, ChangeRequest.REGISTER, SelectionKey.OP_CONNECT));
		}
		
		return socketChannel;
	}

	private Selector initSelector() throws IOException {
		// Create a new selector
		logger.trace("opening selector");
		SelectorProvider sp = SelectorProvider.provider();
		logger.trace("SelectorProvider class: "+sp.getClass().getName());
		return sp.openSelector();
	}

}
