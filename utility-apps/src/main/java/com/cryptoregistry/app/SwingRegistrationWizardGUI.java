/*
 *  This file is part of Buttermilk
 *  Copyright 2011-2015 David R. Smith All Rights Reserved.
 *
 */
package com.cryptoregistry.app;

import java.awt.Dimension;
import java.io.InputStream;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.UIManager;
import javax.swing.UIManager.LookAndFeelInfo;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import asia.redact.bracket.properties.Properties;

public class SwingRegistrationWizardGUI {
	
	 public static JTabbedPane tabbedPane;
	 public static ShowHelpPanel showHelpPanel;
     public static SettingsPanel settingsPanel;
     public static RegHandlePanel regHandlePanel;
     public static CreateKeyPanel createKeyPanel;
     public static PersonalContactPanel personalContactPanel;
     public static BusinessContactPanel businessContactPanel;
     public static WebsiteContactPanel websiteContactPanel;
     public static SignaturePanel signaturePanel;
     public static KeyMaterialsPanel keyMaterialsPanel;
     
     public static KM km;
     

	 private static void createAndShowGUI(Properties props) {
		 
		 km = new KM();
		 
		 try {
			    for (LookAndFeelInfo info : UIManager.getInstalledLookAndFeels()) {
			        if ("Nimbus".equals(info.getName())) {
			            UIManager.setLookAndFeel(info.getClassName());
			            break;
			        }
			    }
			} catch (Exception e) {}
		 
	        //Create and set up the window.
	        JFrame frame = new JFrame("cryptoregistry.com - Registration Key Materials Wizard");
	        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	        frame.setPreferredSize(new Dimension(970,570));
	       
	        tabbedPane = new JTabbedPane();
	        tabbedPane.setPreferredSize(new Dimension(970,350));
	       
	        showHelpPanel = new ShowHelpPanel(props);
	        settingsPanel = new SettingsPanel(props);
	        regHandlePanel = new RegHandlePanel(props);
	        createKeyPanel = new CreateKeyPanel(props);
	        personalContactPanel = new PersonalContactPanel();
	        businessContactPanel = new BusinessContactPanel();
	        websiteContactPanel = new WebsiteContactPanel();
	        signaturePanel = new SignaturePanel();
	        keyMaterialsPanel = new KeyMaterialsPanel(props);
	        
	    	settingsPanel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
	    	regHandlePanel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
	    	createKeyPanel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
	    	personalContactPanel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
	    	businessContactPanel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
	    	websiteContactPanel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
	    	signaturePanel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
	        
	        tabbedPane.addTab("Introduction", showHelpPanel);
	        tabbedPane.addTab("Settings", settingsPanel);
	        tabbedPane.addTab("Select Registration Handle", regHandlePanel);
	        tabbedPane.addTab("Initial Key", createKeyPanel);
	        tabbedPane.addTab("Personal Contact", personalContactPanel);
	        tabbedPane.addTab("Business Contact", businessContactPanel);
	        tabbedPane.addTab("Website Contact", websiteContactPanel);
	        tabbedPane.addTab("Signature", signaturePanel);
	        tabbedPane.addTab("Generated Key Materials", keyMaterialsPanel);
	        
	        ChangeListener changeListener = new ChangeListener() {

				@Override
				public void stateChanged(ChangeEvent e) {
					 JTabbedPane sourceTabbedPane = (JTabbedPane) e.getSource();
				        int index = sourceTabbedPane.getSelectedIndex();
				        switch(index){
				        	case 0: {
				        		// introduction tab
				        		break;
				        	}
				        	case 1:{
				        		settingsPanel.getTextField().requestFocusInWindow();
				        		break;
				        	}
				        	case 2:{
				        		regHandlePanel.getRegHandleTextField().requestFocusInWindow();
				        		break;
				        	}
				        	case 3:{
				        		createKeyPanel.getPassword0().requestFocusInWindow();
				        		break;
				        	}
				        	case 4:{
				        		personalContactPanel.getTextField().requestFocusInWindow();
				        		break;
				        	}
				        	case 5:{
				        		businessContactPanel.getTextField().requestFocusInWindow();
				        		break;
				        	}
				        	case 6:{
				        		websiteContactPanel.getTextField().requestFocusInWindow();
				        		break;
				        	}
				        	case 7:{
				        	//	websiteContactPanel.getTextField().requestFocusInWindow();
				        		break;
				        	}
				        	case 8:{
				        		keyMaterialsPanel.getTextPane().requestFocusInWindow();
				        		break;
				        	}
				        	default: {}
				        }
					
				}
	        };
	        
	        tabbedPane.addChangeListener(changeListener);
	        
	        JPanel outer = new JPanel();
	        outer.setLayout(new BoxLayout(outer, BoxLayout.LINE_AXIS));
	        outer.add(tabbedPane);
	        outer.add(new ChecklistPanel(km));
	        
	        frame.getContentPane().add(outer);
	        
	        //Display the window.
	        frame.pack();
	        frame.setVisible(true);
	        
	      
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
