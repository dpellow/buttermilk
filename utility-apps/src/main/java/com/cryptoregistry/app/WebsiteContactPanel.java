/*
 *  This file is part of Buttermilk
 *  Copyright 2011-2015 David R. Smith All Rights Reserved.
 *
 */
package com.cryptoregistry.app;


import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.concurrent.ExecutionException;

import javax.swing.DefaultComboBoxModel;
import javax.swing.JPanel;
import javax.swing.GroupLayout;
import javax.swing.SwingWorker;
import javax.swing.GroupLayout.Alignment;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.LayoutStyle.ComponentPlacement;
import javax.swing.JButton;
import javax.swing.JTextField;

import com.cryptoregistry.CryptoContact;

public class WebsiteContactPanel extends JPanel {

	private static final long serialVersionUID = 1L;
	private JTextField nameTextField;
	private JTextField orgTextField;
	private JTextField phone0TextField;
	private JTextField fax0TextField;
	private JTextField email0TextField;
	private JTextField address0TextField;
	private JTextField address1TextField;
	private JTextField cityTextField;
	private JTextField stateTextField;
	private JTextField countryTextField;
	private JTextField postalCodeTextField;
	
	private CryptoContact contact;
	
	private JButton btnCreate;
	private JComboBox<String> comboBox;

	public WebsiteContactPanel() {
		
		comboBox = new JComboBox<String>();
		String [] whoisRecordTypes = {"REGISTRANT","ADMINISTRATIVE", "BUSINESS", "TECHNICAL"};
		DefaultComboBoxModel<String> model = new DefaultComboBoxModel<String>(whoisRecordTypes);
		comboBox.setModel(model);
		
		JLabel lblType = new JLabel("Type");
		
		JLabel lblName = new JLabel("Name");
		
		JLabel lblOrganization = new JLabel("Organization");
		
		JLabel lblAddress = new JLabel("Address.0");
		
		JLabel lblAddress_1 = new JLabel("Address.1");
		
		JLabel lblCity = new JLabel("City");
		
		JLabel lblStateorprovince = new JLabel("State/Province");
		
		JLabel lblCountryoreconomy = new JLabel("Country/Economy");
		
		JLabel lblPostalcode = new JLabel("PostalCode");
		
		JLabel lblPhonenumber = new JLabel("Phone.0");
		
		JLabel lblFax = new JLabel("Fax.0");
		
		JLabel lblEmail = new JLabel("Email.0");
		
		nameTextField = new JTextField();
		nameTextField.setColumns(10);
		
		orgTextField = new JTextField();
		orgTextField.setColumns(10);
		
		phone0TextField = new JTextField();
		phone0TextField.setColumns(10);
		
		fax0TextField = new JTextField();
		fax0TextField.setColumns(10);
		
		email0TextField = new JTextField();
		email0TextField.setColumns(10);
		
		address0TextField = new JTextField();
		address0TextField.setColumns(10);
		
		address1TextField = new JTextField();
		address1TextField.setColumns(10);
		
		cityTextField = new JTextField();
		cityTextField.setColumns(10);
		
		stateTextField = new JTextField();
		stateTextField.setColumns(10);
		
		countryTextField = new JTextField();
		countryTextField.setColumns(10);
		
		postalCodeTextField = new JTextField();
		postalCodeTextField.setColumns(10);
		
		btnCreate = new JButton("Create Website Contact");
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
						 
						 btnCreate.setText("Create Website Contact");
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
					.addGroup(groupLayout.createParallelGroup(Alignment.TRAILING)
						.addGroup(groupLayout.createSequentialGroup()
							.addGroup(groupLayout.createParallelGroup(Alignment.LEADING)
								.addComponent(lblName)
								.addComponent(lblOrganization)
								.addComponent(lblCountryoreconomy)
								.addComponent(lblStateorprovince)
								.addComponent(lblPostalcode)
								.addComponent(lblEmail)
								.addComponent(lblPhonenumber)
								.addComponent(lblFax)
								.addComponent(lblType))
							.addGap(24)
							.addGroup(groupLayout.createParallelGroup(Alignment.LEADING)
								.addComponent(comboBox, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
								.addGroup(groupLayout.createParallelGroup(Alignment.LEADING, false)
									.addComponent(orgTextField)
									.addComponent(nameTextField, GroupLayout.DEFAULT_SIZE, 224, Short.MAX_VALUE)
									.addComponent(stateTextField, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
									.addComponent(countryTextField, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
									.addComponent(postalCodeTextField, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
									.addComponent(address0TextField)
									.addComponent(address1TextField)
									.addComponent(cityTextField))
								.addGroup(groupLayout.createParallelGroup(Alignment.TRAILING, false)
									.addComponent(phone0TextField, Alignment.LEADING)
									.addComponent(fax0TextField, Alignment.LEADING)
									.addComponent(email0TextField, Alignment.LEADING, GroupLayout.DEFAULT_SIZE, 169, Short.MAX_VALUE)))
							.addContainerGap(71, Short.MAX_VALUE))
						.addGroup(groupLayout.createSequentialGroup()
							.addComponent(btnCreate)
							.addContainerGap())
						.addGroup(groupLayout.createSequentialGroup()
							.addComponent(lblAddress)
							.addContainerGap(356, Short.MAX_VALUE))
						.addGroup(groupLayout.createSequentialGroup()
							.addComponent(lblAddress_1)
							.addContainerGap(356, Short.MAX_VALUE))
						.addGroup(groupLayout.createSequentialGroup()
							.addComponent(lblCity)
							.addContainerGap(386, Short.MAX_VALUE))))
		);
		groupLayout.setVerticalGroup(
			groupLayout.createParallelGroup(Alignment.LEADING)
				.addGroup(groupLayout.createSequentialGroup()
					.addContainerGap()
					.addGroup(groupLayout.createParallelGroup(Alignment.BASELINE)
						.addComponent(lblType)
						.addComponent(comboBox, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
					.addPreferredGap(ComponentPlacement.RELATED)
					.addGroup(groupLayout.createParallelGroup(Alignment.BASELINE)
						.addComponent(nameTextField, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
						.addComponent(lblName))
					.addPreferredGap(ComponentPlacement.RELATED)
					.addGroup(groupLayout.createParallelGroup(Alignment.BASELINE)
						.addComponent(orgTextField, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
						.addComponent(lblOrganization))
					.addPreferredGap(ComponentPlacement.RELATED)
					.addGroup(groupLayout.createParallelGroup(Alignment.BASELINE)
						.addComponent(lblAddress)
						.addComponent(address0TextField, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
					.addPreferredGap(ComponentPlacement.RELATED)
					.addGroup(groupLayout.createParallelGroup(Alignment.BASELINE)
						.addComponent(lblAddress_1)
						.addComponent(address1TextField, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
					.addPreferredGap(ComponentPlacement.RELATED)
					.addGroup(groupLayout.createParallelGroup(Alignment.BASELINE)
						.addComponent(lblCity)
						.addComponent(cityTextField, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
					.addPreferredGap(ComponentPlacement.RELATED)
					.addGroup(groupLayout.createParallelGroup(Alignment.BASELINE)
						.addComponent(stateTextField, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
						.addComponent(lblStateorprovince))
					.addPreferredGap(ComponentPlacement.RELATED)
					.addGroup(groupLayout.createParallelGroup(Alignment.BASELINE)
						.addComponent(countryTextField, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
						.addComponent(lblCountryoreconomy))
					.addPreferredGap(ComponentPlacement.RELATED)
					.addGroup(groupLayout.createParallelGroup(Alignment.BASELINE)
						.addComponent(postalCodeTextField, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
						.addComponent(lblPostalcode))
					.addGap(16)
					.addGroup(groupLayout.createParallelGroup(Alignment.BASELINE)
						.addComponent(lblPhonenumber)
						.addComponent(phone0TextField, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
					.addPreferredGap(ComponentPlacement.RELATED)
					.addGroup(groupLayout.createParallelGroup(Alignment.BASELINE)
						.addComponent(lblFax)
						.addComponent(fax0TextField, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
					.addPreferredGap(ComponentPlacement.RELATED)
					.addGroup(groupLayout.createParallelGroup(Alignment.BASELINE)
						.addComponent(lblEmail)
						.addComponent(email0TextField, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
					.addPreferredGap(ComponentPlacement.RELATED, 24, Short.MAX_VALUE)
					.addComponent(btnCreate)
					.addContainerGap())
		);
		setLayout(groupLayout);
	}

	public JTextField getTextField() {
		return nameTextField;
	}
	
	private void createContactRecord() {
		CryptoContact contact = new CryptoContact();
		contact.add("contactType", "Website");
		contact.add("WhoisRecordType", (String) comboBox.getSelectedItem());
		contact.add("Name", this.nameTextField.getText());
		contact.add("Organization", this.orgTextField.getText());
		contact.add("Address.0", this.address0TextField.getText());
		contact.add("Address.1", this.address1TextField.getText());
		contact.add("City", this.cityTextField.getText());
		contact.add("State", this.stateTextField.getText());
		contact.add("Country",this.countryTextField.getText());
		contact.add("PostalCode", this.postalCodeTextField.getText());
		contact.add("Phone.0", this.phone0TextField.getText());
		contact.add("Fax.0", this.fax0TextField.getText());
		contact.add("Email.0", this.email0TextField.getText());
	
		this.contact = contact;
	}
}
