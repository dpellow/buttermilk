package com.cryptoregistry.btls.client;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Frame;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

import com.cryptoregistry.btls.handshake.HandshakeProtocol;

/**
 * Settings for the client
 * 
 * @author Dave
 *
 */
public class ConnectionDialog extends JDialog implements ActionListener {

	private static final long serialVersionUID = 1L;
	
	private int port = 4444;
	private String host = "localhost";
	private HandshakeProtocol selectedHp = HandshakeProtocol.H2;
	private String dbClientPath = "C:/Users/Dave/workspace-cryptoregistry/buttermilk/client-storage/data";
	
	private JTextArea hDescField;

	public ConnectionDialog()  {
	}

	public ConnectionDialog(Frame parent) {
		super(parent, true);
		  if (parent != null) {
		      Dimension parentSize = parent.getSize(); 
		      Point p = parent.getLocation(); 
		      setLocation(p.x + parentSize.width / 4, p.y + parentSize.height / 4);
		  }
		  this.setTitle("Connection Dialog");
		  
		  JPanel mainPanel = new JPanel();
		  mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.PAGE_AXIS));
		  
		  JPanel handshakePanel = new JPanel();
		  handshakePanel.setBorder(BorderFactory.createTitledBorder("Handshake"));
		  
		  JComboBox<HandshakeProtocol> list = new JComboBox<HandshakeProtocol>(HandshakeProtocol.values());
		  list.setSelectedIndex(2);
		  list.addActionListener(this);
		  list.setActionCommand("protocol");
		  
		  JScrollPane listScroller = new JScrollPane(list);
		  handshakePanel.add(new JLabel("Available Handshake Protocols:"));
		  handshakePanel.add(listScroller);
		  
		  JPanel descPanel = new JPanel();
		  hDescField = new JTextArea(2,30);
		  hDescField.setEditable(false);
		  hDescField.setWrapStyleWord(true);
		  hDescField.setLineWrap(true);
		  hDescField.setText(selectedHp.getDescription());
		  descPanel.add(hDescField);
		  handshakePanel.add(descPanel);
		  
		  JPanel conPanel = new JPanel();
		  conPanel.setBorder(BorderFactory.createTitledBorder("Server and Port"));
		  
		  JPanel portPanel = new JPanel();
		  JLabel portLabel = new JLabel("Port:");
		  JTextField portText = new JTextField(6);
		  portText.addActionListener(this);
		  portText.setActionCommand("port");
		  portText.setText(String.valueOf(port));
		  
		  portPanel.add(portLabel);
		  portPanel.add(portText);
		  
		  JPanel hostPanel = new JPanel();
		  JLabel hostLabel = new JLabel("Server Hostname:");
		  JTextField hostText = new JTextField(30);
		  hostText.setText(host);
		  hostText.addActionListener(this);
		  hostText.setActionCommand("host");
		  
		  JPanel dbPanel = new JPanel();
		  JLabel dbLabel = new JLabel("DB Path:");
		  JTextField dbPathText = new JTextField(50);
		  dbPathText.setText(dbClientPath);
		  dbPathText.addActionListener(this);
		  dbPathText.setActionCommand("dbPath");
		  dbPanel.add(dbLabel);
		  dbPanel.add(dbPathText);
		  dbPanel.setBorder(BorderFactory.createTitledBorder("Security Datastore"));
		  
		  JPanel controls = new JPanel();
		  JButton ok = new JButton("Done");
		  ok.setActionCommand("done");
		  ok.addActionListener(this);
		  controls.add(ok);

		  hostPanel.add(hostLabel);
		  hostPanel.add(hostText);
		  
		  conPanel.add(portPanel);
		  conPanel.add(hostPanel);
		  
		  mainPanel.add(handshakePanel);
		  mainPanel.add(conPanel);
		  mainPanel.add(dbPanel);
		  mainPanel.add(controls);
		  
		  this.getContentPane().add(mainPanel,BorderLayout.CENTER);
		  
		  setDefaultCloseOperation(HIDE_ON_CLOSE);
		  pack(); 
		  setVisible(false);
	}

	public int getPort() {
		return port;
	}

	public String getHost() {
		return host;
	}

	public HandshakeProtocol getSelectedHp() {
		return selectedHp;
	}

	@SuppressWarnings("unchecked")
	@Override
	public void actionPerformed(ActionEvent e) {
		String cmd = e.getActionCommand();
		switch(cmd){
			case "protocol":{
				JComboBox<HandshakeProtocol> box = (JComboBox<HandshakeProtocol>) e.getSource();
				selectedHp = (HandshakeProtocol) box.getSelectedItem();
				this.hDescField.setText(selectedHp.description);
				break;
			}
			case "port" : {
				JTextField tf = (JTextField) e.getSource();
				host = tf.getText();
				break;
			}
			case "host" : {
				JTextField tf = (JTextField) e.getSource();
				port = Integer.parseInt(tf.getText());
				break;
			}
			
			case "dbPath" : {
				JTextField tf = (JTextField) e.getSource();
				dbClientPath = tf.getText();
				break;
			}
			case "done" : {
				this.setVisible(false);
				break;
			}
			default: break;
		}
		
	}

	public String getDbClientPath() {
		return dbClientPath;
	}
	
	

}
