package com.cryptoregistry.app;

import java.awt.Dimension;
import java.io.InputStream;

import javax.swing.BoxLayout;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTextArea;

import asia.redact.bracket.properties.Properties;

public class SwingRegistrationBuilder {
	
	 static JTextArea outputPane;

	 private static void createAndShowGUI(Properties props) {
	        //Create and set up the window.
	        JFrame frame = new JFrame("cryptoregistry.com - Registration Key Materials Wizard");
	        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	        frame.setPreferredSize(new Dimension(800,500));
	       
	        JTabbedPane tabbedPane = new JTabbedPane();
	        RegHandlePanel regHandlePanel = new RegHandlePanel(props);
	        tabbedPane.addTab("Introduction", new ShowHelpPanel(props));
	        tabbedPane.addTab("Select Registration Handle", regHandlePanel);
	        tabbedPane.addTab("Initial Key", new CreateKeyPanel(props));
	        tabbedPane.addTab("Personal Contact", new PersonalContactPanel());
	        tabbedPane.addTab("Business Contact", new BusinessContactPanel());
	        tabbedPane.addTab("Signature", new SignaturePanel());
	        
	        JPanel outer = new JPanel();
	        outer.setLayout(new BoxLayout(outer, BoxLayout.PAGE_AXIS));
	        outer.add(tabbedPane);
	        
	        outputPane = new JTextArea();
	        outputPane.setPreferredSize(new Dimension(800,200));
	        outputPane.setEditable(false);
	        JScrollPane scroll = new JScrollPane(outputPane);
	        outer.add(scroll);
	        
	        frame.getContentPane().add(outer);
	        
	        //Display the window.
	        frame.pack();
	        frame.setVisible(true);
	        regHandlePanel.getRegHandleTextField().requestFocusInWindow();
	    }
	 
	    public static void main(String[] args) {
	        javax.swing.SwingUtilities.invokeLater(new Runnable() {
	            public void run() {
	            	InputStream in = Thread.currentThread()
	            			.getContextClassLoader().getResourceAsStream("regwizard.properties");
	            	Properties props = Properties.Factory.getInstance(in);
	                createAndShowGUI(props);
	            }
	        });
	    }

}
