package com.cryptoregistry.btls.client;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.Socket;
import java.util.Date;

import javax.swing.*;

import com.cryptoregistry.btls.SecureClientSocketBuilder;
import com.cryptoregistry.btls.handshake.HandshakeFailedException;
import com.cryptoregistry.client.security.Datastore;
import com.cryptoregistry.client.storage.BDBDatastore;
import com.cryptoregistry.client.storage.SimpleKeyManager;

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
	Datastore ds;
	JTextArea textArea;
	JTextField input;
	JButton send;
	Thread il = null;

	public EchoClientSwingGUI() {
		super();
		createAndShowGUI();
		cDialog = new ConnectionDialog(frame);
	}

	private void createAndShowGUI() {
		// Create and set up the window.
		frame = new JFrame("Echo Chat Client");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

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

	@Override
	public void actionPerformed(ActionEvent e) {
		String cmd = e.getActionCommand();
		switch (cmd) {
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
		}

		default:
			break;
		}

	}

	private void socketConnect() {

		Date start = new Date();
		
		SimpleKeyManager km = new SimpleKeyManager(cDialog.getDbClientPath());
		ds = new BDBDatastore(km);
		
		Date end = new Date();
		long ms = end.getTime() - start.getTime();
		System.err.println("Datastore load completed in "+ms+" ms");

		try {

			socket = null;
			SecureClientSocketBuilder connector = new SecureClientSocketBuilder(
					cDialog.getSelectedHp(), ds, new Socket(cDialog.getHost(),
							cDialog.getPort()));

			socket = connector.buildSecure();
			startListening(socket);
		} catch (HandshakeFailedException | IOException e1) {
			e1.printStackTrace();
		}

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
		} finally {
			if (ds != null) {
				ds.close();
				ds = null;
			}
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
