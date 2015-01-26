package com.cryptoregistry.btls.client;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.ConnectException;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.Set;

import javax.swing.*;

import com.cryptoregistry.btls.SecureClientSocketBuilder;
import com.cryptoregistry.btls.handshake.HandshakeFailedException;
import com.cryptoregistry.c2.key.Curve25519KeyContents;
import com.cryptoregistry.client.security.Datastore;
import com.cryptoregistry.client.storage.BDBDatastore;
import com.cryptoregistry.client.storage.Handle;
import com.cryptoregistry.client.storage.Metadata;
import com.cryptoregistry.client.storage.SimpleKeyManager;
import com.cryptoregistry.ec.ECKeyContents;
import com.cryptoregistry.rsa.RSAKeyContents;
import com.cryptoregistry.util.RandomStringGenerator;

/**
 * Swing GUI echo client
 * 
 * @author Dave
 *
 */
public class EchoClientSwingGUI implements ActionListener {

	ConnectionDialog cDialog;
	JFrame frame;
	Socket socket;
	JTextArea textArea;
	JTextField input;
	JButton send;
	Thread il = null;
	
	Datastore ds;
	
	RandomStringGenerator stringGen;
	
	private String dbClientPath = "C:/Users/Dave/workspace-cryptoregistry/buttermilk/client-storage/data";

	public EchoClientSwingGUI() {
		super();
		initDb();
		createAndShowGUI();
		cDialog = new ConnectionDialog(frame);
	}
	
	private void initDb() {
		SimpleKeyManager km = new SimpleKeyManager(this.dbClientPath);
		ds = new BDBDatastore(km);
	}
	
	private void closeDs() {
		System.err.println("Closing Datasource");
		if(ds != null) {
			ds.close();	
		}
	}

	private void createAndShowGUI() {
		// Create and set up the window.
		frame = new JFrame("Echo Chat Client");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		 frame.addWindowListener(new WindowAdapter() {
	         public void windowClosing(WindowEvent windowEvent){
	        	closeDs();
		        System.exit(0);
	         }        
	      });  

		// Create the menu bar. Make it have a green background.
		JMenuBar greenMenuBar = new JMenuBar();
		greenMenuBar.setOpaque(true);
		greenMenuBar.setBackground(new Color(154, 165, 127));
		greenMenuBar.setPreferredSize(new Dimension(200, 20));
		JMenu menu = new JMenu("Connections");
		greenMenuBar.add(menu);
		JMenuItem connectDialogItem = new JMenuItem("Settings...");
		connectDialogItem.addActionListener(this);
		connectDialogItem.setActionCommand("showDialog");
		menu.add(connectDialogItem);
		menu.addSeparator();
		JMenuItem connectItem = new JMenuItem("Connect");
		connectItem.addActionListener(this);
		connectItem.setActionCommand("connect");
		menu.add(connectItem);
		menu.addSeparator();
		JMenuItem disconnectItem = new JMenuItem("Disconnect");
		disconnectItem.addActionListener(this);
		disconnectItem.setActionCommand("disconnect");
		menu.add(disconnectItem);
		
		JMenu utilmenu = new JMenu("Tools");
		greenMenuBar.add(utilmenu);
		
		JMenuItem dbListItem = new JMenuItem("List Keys");
		dbListItem.addActionListener(this);
		dbListItem.setActionCommand("listKeys");
		utilmenu.add(dbListItem);
		utilmenu.addSeparator();
		
		JMenuItem createC2Item = new JMenuItem("Create Curve25519 Key");
		createC2Item.addActionListener(this);
		createC2Item.setActionCommand("createC2Key");
		utilmenu.add(createC2Item);
		
		JMenuItem createECItem = new JMenuItem("Create Elliptic Curve Key");
		createECItem.addActionListener(this);
		createECItem.setActionCommand("createECKey");
		utilmenu.add(createECItem);
		
		JMenuItem createRSAItem = new JMenuItem("Create RSA Key");
		createRSAItem.addActionListener(this);
		createRSAItem.setActionCommand("createRSAKey");
		utilmenu.add(createRSAItem);
		utilmenu.addSeparator();
		
		JMenuItem clearItem = new JMenuItem("Clear Text Area");
		clearItem.addActionListener(this);
		clearItem.setActionCommand("clear");
		utilmenu.add(clearItem);
		
		JMenu testmenu = new JMenu("Tests");
		greenMenuBar.add(testmenu);
		
		JMenuItem test1Item = new JMenuItem("Test1 - 1 Megabyte");
		test1Item.addActionListener(this);
		test1Item.setActionCommand("test1");
		testmenu.add(test1Item);
		

		input = new JTextField(50);
		input.addActionListener(this);
		input.setActionCommand("sendLine");
		input.setEnabled(false);

		send = new JButton("Send");
		send.setEnabled(false);
		send.addActionListener(this);
		send.setActionCommand("sendLineButton");
		JPanel controls = new JPanel();

		controls.add(input);
		controls.add(send);

		textArea = new JTextArea(5, 20);
		JScrollPane scroll = new JScrollPane(textArea);
		textArea.setFont(new Font("Serif", Font.ITALIC, 10));
		textArea.setLineWrap(true);
		textArea.setWrapStyleWord(true);
		textArea.setEditable(false);

		// Set the menu bar and add the label to the content pane.
		frame.setJMenuBar(greenMenuBar);
		frame.getContentPane().add(controls, BorderLayout.NORTH);
		frame.getContentPane().add(scroll, BorderLayout.CENTER);

		// Display the window.
		frame.pack();
		frame.setVisible(true);
	}
	
	private void test1() {
		textArea.setText("");
		textArea.append("Creating data...");
		if(stringGen == null) {
			stringGen = new RandomStringGenerator();
		}
		textArea.append("Sending data...");
		ArrayList<String> list = stringGen.next(60, (int) Math.floor(1024000/60));
		for(int i = 0; i<list.size();i++){
			String item = list.get(i)+"\n";
			try {
				socket.getOutputStream().write(item.getBytes("UTF-8"));
				socket.getOutputStream().flush(); 
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
			
		}
		
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		String cmd = e.getActionCommand();
		switch (cmd) {
		
		case "test1": {
			test1();
			break;
		}
		
		case "createC2Key": {
			Curve25519KeyContents c1 = com.cryptoregistry.c2.CryptoFactory.INSTANCE.generateKeys();
			ds.getViews().put(ds.getRegHandle(), c1);
			break;
		}
		
		case "createECKey": {
			ECKeyContents c2 = com.cryptoregistry.ec.CryptoFactory.INSTANCE.generateKeys("P-256");
			ds.getViews().put(ds.getRegHandle(), c2);
			break;
		}
		
		case "createRSAKey": {
			RSAKeyContents c2 = com.cryptoregistry.rsa.CryptoFactory.INSTANCE.generateKeys();
			ds.getViews().put(ds.getRegHandle(), c2);
			break;
		}
		
		case "clear": {
			textArea.setText("");
			break;
		}
		
		case "showDialog": {
			cDialog.setVisible(true);
			break;
		}
		case "connect": {
			socketConnect();
			break;
		}
		case "disconnect": {
			socketDisconnect();
			break;
		}
		case "sendLine": {
			JTextField input = (JTextField) e.getSource();
			try {
				if (socket == null || socket.isClosed())
					break;
				String msg = input.getText();
				msg = msg.trim();
				if (msg == null || msg.equals("") || msg.length() ==0) break;
				
				socket.getOutputStream().write(msg.getBytes("UTF-8"));
				socket.getOutputStream().flush(); // sends it
				input.setText("");
			} catch (UnsupportedEncodingException e1) {
				e1.printStackTrace();
			} catch (IOException e1) {
				e1.printStackTrace();
			}
			break;
		}

		case "sendLineButton": {

			try {
				if (socket == null || socket.isClosed())
					break;
				String msg = input.getText();
				msg = msg.trim();
				if (msg == null || msg.equals("") || msg.length() ==0) break;
				
				socket.getOutputStream().write(msg.getBytes("UTF-8"));
				socket.getOutputStream().flush(); // sends it
				input.setText("");
			} catch (UnsupportedEncodingException e1) {
				e1.printStackTrace();
			} catch (IOException e1) {
				e1.printStackTrace();
			}
			break;
		}
		
		case "listKeys" : {
			listKeysInDb();
			break;
		}

		default:
			break;
		}

	}

	private void socketConnect() {

		Date start = new Date();
		
	//	SimpleKeyManager km = new SimpleKeyManager(cDialog.getDbClientPath());
	//	ds = new BDBDatastore(km);
		
		Date end = new Date();
		long ms = end.getTime() - start.getTime();
		System.err.println("Datastore load completed in "+ms+" ms");

		try {

			socket = null;
			SecureClientSocketBuilder connector = new SecureClientSocketBuilder(
					cDialog.getSelectedHp(), ds, new Socket(cDialog.getHost(), cDialog.getPort()));

			socket = connector.buildSecure();
			startListening(socket);
		} catch (HandshakeFailedException | IOException e1) {
			if(e1 instanceof ConnectException){
				JOptionPane.showMessageDialog(frame,
					    "Server may not be running.",
					    "ConnectionException",
					    JOptionPane.WARNING_MESSAGE);
			}
			
			// print stacktrace and bail out
			e1.printStackTrace();
			return;
		} 

		// normal, turn on input field and button
		input.setEnabled(true);
		send.setEnabled(true);
	}

	private void socketDisconnect() {
		try {
			if (!socket.isClosed())
				socket.close();
			stopListening();
		} catch (IOException e) {
			e.printStackTrace();
		} 

		input.setEnabled(false);
		send.setEnabled(false);

	}

	private void startListening(Socket socket) {
		il = new Thread(new ClientWorker(socket, textArea));
		il.start();
	}

	private void stopListening() {
		il.interrupt();
		il = null;
	}
	
	private void listKeysInDb(){
		textArea.setText("");
		textArea.append("Keys:\n");
		Set<Handle> keys = ds.getViews().getMetadataMap().keySet();
		Iterator<Handle> iter = keys.iterator();
		while(iter.hasNext()){
			Handle h = iter.next();
			Metadata m = ds.getViews().getMetadataMap().get(h);
			textArea.append(h.getHandle()+": "+m.toString()+"\n");
		}
	}

	public static void main(String[] args) {
		// Schedule a job for the event-dispatching thread:
		// creating and showing this application's GUI.
		javax.swing.SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				new EchoClientSwingGUI();
			}
		});
	}
}
