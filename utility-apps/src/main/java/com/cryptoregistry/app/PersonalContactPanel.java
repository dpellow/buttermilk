package com.cryptoregistry.app;

import javax.swing.JPanel;
import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.LayoutStyle.ComponentPlacement;
import javax.swing.JButton;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import javax.swing.JCheckBox;

public class PersonalContactPanel extends JPanel {

	private static final long serialVersionUID = 1L;
	private JTextField textField_7;
	private JTextField textField_10;
	private JTextField textField;
	private JTextField textField_1;
	private JTextField textField_2;

	public PersonalContactPanel() {
		super();
		
		JLabel lblCountry = new JLabel("Country");
		
		JLabel lblEmail = new JLabel("Email.0");
		
		textField_7 = new JTextField();
		textField_7.setColumns(10);
		
		textField_10 = new JTextField();
		textField_10.setColumns(10);
		
		JButton btnCreate = new JButton("Create");
		btnCreate.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				System.err.println("Create Contact Record");
			}
		});
		
		JLabel lblGivenname = new JLabel("GivenName.0");
		
		textField = new JTextField();
		textField.setColumns(10);
		
		JLabel lblFamilyname = new JLabel("FamilyName.0");
		
		textField_1 = new JTextField();
		textField_1.setColumns(10);
		
		JCheckBox chckbxIPreferTo = new JCheckBox("I prefer to remain anonymous, please create an empty contact record.");
		
		JLabel lblMobile = new JLabel("Mobile.0");
		
		textField_2 = new JTextField();
		textField_2.setColumns(10);
		
		GroupLayout groupLayout = new GroupLayout(this);
		groupLayout.setHorizontalGroup(
			groupLayout.createParallelGroup(Alignment.TRAILING)
				.addGroup(groupLayout.createSequentialGroup()
					.addContainerGap(364, Short.MAX_VALUE)
					.addComponent(btnCreate)
					.addGap(21))
				.addGroup(Alignment.LEADING, groupLayout.createSequentialGroup()
					.addGap(19)
					.addGroup(groupLayout.createParallelGroup(Alignment.LEADING)
						.addComponent(chckbxIPreferTo)
						.addGroup(groupLayout.createSequentialGroup()
							.addGroup(groupLayout.createParallelGroup(Alignment.LEADING)
								.addComponent(lblCountry)
								.addComponent(lblEmail)
								.addComponent(lblGivenname)
								.addComponent(lblFamilyname)
								.addComponent(lblMobile))
							.addGap(23)
							.addGroup(groupLayout.createParallelGroup(Alignment.LEADING)
								.addComponent(textField_10, GroupLayout.PREFERRED_SIZE, 258, GroupLayout.PREFERRED_SIZE)
								.addGroup(groupLayout.createParallelGroup(Alignment.TRAILING, false)
									.addComponent(textField_1, Alignment.LEADING)
									.addComponent(textField, Alignment.LEADING)
									.addComponent(textField_7, Alignment.LEADING, GroupLayout.DEFAULT_SIZE, 170, Short.MAX_VALUE))
								.addComponent(textField_2, GroupLayout.PREFERRED_SIZE, 168, GroupLayout.PREFERRED_SIZE))))
					.addContainerGap(64, Short.MAX_VALUE))
		);
		groupLayout.setVerticalGroup(
			groupLayout.createParallelGroup(Alignment.TRAILING)
				.addGroup(groupLayout.createSequentialGroup()
					.addContainerGap()
					.addGroup(groupLayout.createParallelGroup(Alignment.LEADING)
						.addComponent(lblGivenname)
						.addComponent(textField, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
					.addPreferredGap(ComponentPlacement.RELATED)
					.addGroup(groupLayout.createParallelGroup(Alignment.BASELINE)
						.addComponent(lblFamilyname)
						.addComponent(textField_1, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
					.addGap(29)
					.addGroup(groupLayout.createParallelGroup(Alignment.BASELINE)
						.addComponent(lblCountry)
						.addComponent(textField_7, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
					.addPreferredGap(ComponentPlacement.RELATED)
					.addGroup(groupLayout.createParallelGroup(Alignment.BASELINE)
						.addComponent(textField_10, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
						.addComponent(lblEmail))
					.addPreferredGap(ComponentPlacement.RELATED)
					.addGroup(groupLayout.createParallelGroup(Alignment.BASELINE)
						.addComponent(lblMobile)
						.addComponent(textField_2, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
					.addGap(62)
					.addComponent(chckbxIPreferTo)
					.addPreferredGap(ComponentPlacement.RELATED, 93, Short.MAX_VALUE)
					.addComponent(btnCreate)
					.addContainerGap())
		);
		setLayout(groupLayout);
		
	}

	public JTextField getTextField() {
		return textField;
	}
	
	
}
