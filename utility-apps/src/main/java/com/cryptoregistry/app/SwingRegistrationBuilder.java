package com.cryptoregistry.app;

import java.awt.Dimension;
import java.io.InputStream;

import javax.swing.BoxLayout;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTextArea;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import asia.redact.bracket.properties.Properties;

public class SwingRegistrationBuilder {
	
	 static JTextArea outputPane;

	 private static void createAndShowGUI(Properties props) {
	        //Create and set up the window.
	        JFrame frame = new JFrame("cryptoregistry.com - Registration Key Materials Wizard");
	        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	        frame.setPreferredSize(new Dimension(800,500));
	       
	        JTabbedPane tabbedPane = new JTabbedPane();
	        tabbedPane.setPreferredSize(new Dimension(800,350));
	       
	        final ShowHelpPanel showHelpPanel = new ShowHelpPanel(props);
	        final RegHandlePanel regHandlePanel = new RegHandlePanel(props);
	        final CreateKeyPanel createKeyPanel = new CreateKeyPanel(props);
	        final PersonalContactPanel personalContactPanel = new PersonalContactPanel();
	        final BusinessContactPanel businessContactPanel = new BusinessContactPanel();
	        final SignaturePanel signaturePanel = new SignaturePanel();
	        
	        tabbedPane.addTab("Introduction", showHelpPanel);
	        tabbedPane.addTab("Select Registration Handle", regHandlePanel);
	        tabbedPane.addTab("Initial Key", createKeyPanel);
	        tabbedPane.addTab("Personal Contact", personalContactPanel);
	        tabbedPane.addTab("Business Contact", businessContactPanel);
	        tabbedPane.addTab("Signature", signaturePanel);
	        
	        ChangeListener changeListener = new ChangeListener() {

				@Override
				public void stateChanged(ChangeEvent e) {
					 JTabbedPane sourceTabbedPane = (JTabbedPane) e.getSource();
				        int index = sourceTabbedPane.getSelectedIndex();
				        switch(index){
				        	case 0: {
				        	
				        		break;
				        	}
				        	case 1:{
				        		regHandlePanel.getRegHandleTextField().requestFocusInWindow();
				        		break;
				        	}
				        	case 2:{
				        		createKeyPanel.getTextField_1().requestFocusInWindow();
				        		break;
				        	}
				        	case 3:{
				        		personalContactPanel.getTextField().requestFocusInWindow();
				        		break;
				        	}
				        	case 4:{
				        		businessContactPanel.getTextField().requestFocusInWindow();
				        		break;
				        	}
				        	case 5:{
				        	//	signaturePanel.getRegHandleTextField().requestFocusInWindow();
				        		break;
				        	}
				        	default: {}
				        }
					
				}
	        };
	        
	        tabbedPane.addChangeListener(changeListener);
	        
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
	       // regHandlePanel.getRegHandleTextField().requestFocusInWindow();
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
