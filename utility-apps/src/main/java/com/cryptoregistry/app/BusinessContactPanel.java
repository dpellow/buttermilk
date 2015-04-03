/*
 *  This file is part of Buttermilk
 *  Copyright 2011-2015 David R. Smith All Rights Reserved.
 *
 */
package com.cryptoregistry.app;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.concurrent.ExecutionException;

import javax.swing.JPanel;
import javax.swing.GroupLayout;
import javax.swing.SwingWorker;
import javax.swing.GroupLayout.Alignment;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.LayoutStyle.ComponentPlacement;
import javax.swing.JButton;

import com.cryptoregistry.CryptoContact;

public class BusinessContactPanel extends JPanel {

	private static final long serialVersionUID = 1L;
	private JTextField businessNameTextField;
	private JTextField streetAddress0TextField;
	private JTextField streetAddress1TextField;
	private JTextField cityTextField;
	private JTextField stateTextField;
	private JTextField postalCodeTextField;
	private JTextField countryTextField;
	private JTextField contactNameTextField;
	private JTextField phone0TextField;
	private JTextField email0TextField;
	
	private JButton btnCreate;
	
	private CryptoContact contact;

	public BusinessContactPanel() {
		
		JLabel lblBusinessname = new JLabel("BusinessName");
		
		businessNameTextField = new JTextField();
		businessNameTextField.setColumns(10);
		
		JLabel lblStreetaddress = new JLabel("StreetAddress.0");
		
		streetAddress0TextField = new JTextField();
		streetAddress0TextField.setColumns(10);
		
		JLabel lblStreeaddress = new JLabel("StreetAddress.1");
		
		streetAddress1TextField = new JTextField();
		streetAddress1TextField.setColumns(10);
		
		JLabel lblCity = new JLabel("City");
		
		cityTextField = new JTextField();
		cityTextField.setColumns(10);
		
		JLabel lblState = new JLabel("State");
		
		stateTextField = new JTextField();
		stateTextField.setColumns(10);
		
		JLabel lblPostalcode = new JLabel("PostalCode");
		
		postalCodeTextField = new JTextField();
		postalCodeTextField.setColumns(10);
		
		JLabel lblCountry = new JLabel("Country");
		
		countryTextField = new JTextField();
		countryTextField.setColumns(10);
		
		JLabel lblContactname = new JLabel("ContactName.0");
		
		contactNameTextField = new JTextField();
		contactNameTextField.setColumns(10);
		
		JLabel lblBusinessphone = new JLabel("Phone.0");
		
		phone0TextField = new JTextField();
		phone0TextField.setColumns(10);
		
		JLabel lblEmail = new JLabel("Email.0");
		
		email0TextField = new JTextField();
		email0TextField.setColumns(10);
		
		btnCreate = new JButton("Create Business Contact");
		btnCreate.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				btnCreate.setText("Working...");
				btnCreate.setEnabled(false);
				SwingWorker<Void,String> worker = new SwingWorker<Void,String>() {
					protected Void doInBackground() throws Exception {
						createContactRecord();
						return null;
					}
					 @Override
					public void done() {
					  try {
							get();
							if(contact != null)
								SwingRegistrationWizardGUI.km.addContact(contact);
							 
						  } catch (InterruptedException | ExecutionException e) {
							e.printStackTrace();
						  }
						 
						 btnCreate.setText("Create Business Contact");
						 btnCreate.setEnabled(true);
						 SwingRegistrationWizardGUI.tabbedPane.setSelectedIndex(7);
					}
				};
				worker.execute();
				
			}
			
		});
		
		GroupLayout groupLayout = new GroupLayout(this);
		groupLayout.setHorizontalGroup(
			groupLayout.createParallelGroup(Alignment.LEADING)
				.addGroup(groupLayout.createSequentialGroup()
					.addContainerGap()
					.addGroup(groupLayout.createParallelGroup(Alignment.LEADING)
						.addGroup(groupLayout.createSequentialGroup()
							.addComponent(lblBusinessname)
							.addPreferredGap(ComponentPlacement.RELATED)
							.addComponent(businessNameTextField, GroupLayout.PREFERRED_SIZE, 304, GroupLayout.PREFERRED_SIZE))
						.addGroup(groupLayout.createParallelGroup(Alignment.LEADING, false)
							.addGroup(groupLayout.createSequentialGroup()
								.addComponent(lblStreetaddress)
								.addPreferredGap(ComponentPlacement.RELATED)
								.addComponent(streetAddress0TextField, GroupLayout.PREFERRED_SIZE, 233, GroupLayout.PREFERRED_SIZE))
							.addGroup(groupLayout.createSequentialGroup()
								.addGroup(groupLayout.createParallelGroup(Alignment.LEADING)
									.addComponent(lblStreeaddress)
									.addComponent(lblCity))
								.addPreferredGap(ComponentPlacement.RELATED)
								.addGroup(groupLayout.createParallelGroup(Alignment.LEADING)
									.addComponent(cityTextField, GroupLayout.PREFERRED_SIZE, 129, GroupLayout.PREFERRED_SIZE)
									.addComponent(streetAddress1TextField)
									.addGroup(groupLayout.createSequentialGroup()
										.addComponent(stateTextField, GroupLayout.PREFERRED_SIZE, 36, GroupLayout.PREFERRED_SIZE)
										.addPreferredGap(ComponentPlacement.RELATED)
										.addComponent(lblCountry)
										.addPreferredGap(ComponentPlacement.RELATED)
										.addComponent(countryTextField, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
									.addComponent(postalCodeTextField, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))))
						.addComponent(lblState)
						.addComponent(lblPostalcode)
						.addGroup(groupLayout.createSequentialGroup()
							.addComponent(lblContactname)
							.addPreferredGap(ComponentPlacement.RELATED)
							.addGroup(groupLayout.createParallelGroup(Alignment.LEADING, false)
								.addComponent(phone0TextField, GroupLayout.PREFERRED_SIZE, 147, GroupLayout.PREFERRED_SIZE)
								.addComponent(contactNameTextField, GroupLayout.DEFAULT_SIZE, 245, Short.MAX_VALUE)
								.addComponent(email0TextField))))
					.addContainerGap(64, Short.MAX_VALUE))
				.addGroup(groupLayout.createSequentialGroup()
					.addContainerGap(375, Short.MAX_VALUE)
					.addComponent(btnCreate)
					.addContainerGap())
				.addGroup(groupLayout.createSequentialGroup()
					.addContainerGap()
					.addComponent(lblBusinessphone)
					.addContainerGap(400, Short.MAX_VALUE))
				.addGroup(groupLayout.createSequentialGroup()
					.addContainerGap()
					.addComponent(lblEmail)
					.addContainerGap(394, Short.MAX_VALUE))
		);
		groupLayout.setVerticalGroup(
			groupLayout.createParallelGroup(Alignment.LEADING)
				.addGroup(groupLayout.createSequentialGroup()
					.addContainerGap()
					.addGroup(groupLayout.createParallelGroup(Alignment.BASELINE)
						.addComponent(lblBusinessname)
						.addComponent(businessNameTextField, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
					.addGap(26)
					.addGroup(groupLayout.createParallelGroup(Alignment.BASELINE)
						.addComponent(lblStreetaddress)
						.addComponent(streetAddress0TextField, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
					.addPreferredGap(ComponentPlacement.RELATED)
					.addGroup(groupLayout.createParallelGroup(Alignment.BASELINE)
						.addComponent(lblStreeaddress)
						.addComponent(streetAddress1TextField, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
					.addPreferredGap(ComponentPlacement.RELATED)
					.addGroup(groupLayout.createParallelGroup(Alignment.BASELINE)
						.addComponent(lblCity)
						.addComponent(cityTextField, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
					.addPreferredGap(ComponentPlacement.RELATED)
					.addGroup(groupLayout.createParallelGroup(Alignment.BASELINE)
						.addComponent(lblState)
						.addComponent(stateTextField, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
						.addComponent(lblCountry)
						.addComponent(countryTextField, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
					.addPreferredGap(ComponentPlacement.RELATED)
					.addGroup(groupLayout.createParallelGroup(Alignment.BASELINE)
						.addComponent(lblPostalcode)
						.addComponent(postalCodeTextField, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
					.addGap(29)
					.addGroup(groupLayout.createParallelGroup(Alignment.BASELINE)
						.addComponent(lblContactname)
						.addComponent(contactNameTextField, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
					.addPreferredGap(ComponentPlacement.RELATED)
					.addGroup(groupLayout.createParallelGroup(Alignment.BASELINE)
						.addComponent(lblBusinessphone)
						.addComponent(phone0TextField, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
					.addPreferredGap(ComponentPlacement.RELATED)
					.addGroup(groupLayout.createParallelGroup(Alignment.BASELINE)
						.addComponent(lblEmail)
						.addComponent(email0TextField, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
					.addPreferredGap(ComponentPlacement.RELATED, 54, Short.MAX_VALUE)
					.addComponent(btnCreate)
					.addContainerGap())
		);
		setLayout(groupLayout);
		
	}

	public JTextField getTextField() {
		return businessNameTextField;
	}
	
	private void createContactRecord() {
		CryptoContact contact = new CryptoContact();
		contact.add("contactType", "Business");
		contact.add("BusinessName", this.businessNameTextField.getText());
		contact.add("StreetAddress.0", this.streetAddress0TextField.getText());
		contact.add("StreetAddress.1", this.streetAddress1TextField.getText());
		contact.add("City", this.cityTextField.getText());
		contact.add("State", this.stateTextField.getText());
		contact.add("PostalCode", this.postalCodeTextField.getText());
		contact.add("Country",this.countryTextField.getText());
		this.contact = contact;
	}
}
