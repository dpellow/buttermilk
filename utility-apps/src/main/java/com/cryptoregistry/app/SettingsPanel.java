package com.cryptoregistry.app;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;

import javax.swing.JFileChooser;
import javax.swing.JPanel;
import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.LayoutStyle.ComponentPlacement;
import javax.swing.JButton;
import javax.swing.JCheckBox;

import asia.redact.bracket.properties.Properties;

public class SettingsPanel extends JPanel {

	private static final long serialVersionUID = 1L;
	private JTextField textField;
	JFileChooser fc;

	public SettingsPanel(Properties props) {
		
		String userDir = System.getProperties().getProperty("user.dir");
		File defaultPath = new File(new File(userDir), "km");
		String canonical = "";
		
		try {
			canonical = defaultPath.getCanonicalPath();
		} catch (IOException e) {}
		
		JLabel lblKeyMaterialsFolder = new JLabel("Key Materials Folder");
		
		textField = new JTextField(canonical);
		textField.setColumns(10);
		final JPanel _parent = this;
		fc = new JFileChooser();
		fc.setCurrentDirectory(defaultPath);
		fc.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
		
		JButton btnBrowse = new JButton("Browse");
		btnBrowse.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				int returnVal = fc.showOpenDialog(_parent);
				 if (returnVal == JFileChooser.APPROVE_OPTION) {
			            File file = fc.getSelectedFile();
			            try {
							textField.setText(file.getCanonicalPath());
						} catch (IOException e1) {}
			        } else {
			           
			        }
				fc.setVisible(false);
			}
		});
		
		JCheckBox chckbxCreateIfDoes = new JCheckBox("Create folder if does not exist");
		chckbxCreateIfDoes.setSelected(true);
		
		JButton btnOk = new JButton("OK");
		
		GroupLayout groupLayout = new GroupLayout(this);
		groupLayout.setHorizontalGroup(
			groupLayout.createParallelGroup(Alignment.LEADING)
				.addGroup(groupLayout.createSequentialGroup()
					.addContainerGap()
					.addGroup(groupLayout.createParallelGroup(Alignment.LEADING)
						.addComponent(chckbxCreateIfDoes)
						.addGroup(groupLayout.createSequentialGroup()
							.addComponent(lblKeyMaterialsFolder)
							.addPreferredGap(ComponentPlacement.RELATED)
							.addComponent(textField, GroupLayout.DEFAULT_SIZE, 254, Short.MAX_VALUE)
							.addGap(8)
							.addComponent(btnBrowse))
						.addComponent(btnOk, Alignment.TRAILING))
					.addContainerGap())
		);
		groupLayout.setVerticalGroup(
			groupLayout.createParallelGroup(Alignment.LEADING)
				.addGroup(groupLayout.createSequentialGroup()
					.addContainerGap()
					.addGroup(groupLayout.createParallelGroup(Alignment.BASELINE)
						.addComponent(lblKeyMaterialsFolder)
						.addComponent(textField, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
						.addComponent(btnBrowse))
					.addPreferredGap(ComponentPlacement.RELATED)
					.addComponent(chckbxCreateIfDoes)
					.addPreferredGap(ComponentPlacement.RELATED, 207, Short.MAX_VALUE)
					.addComponent(btnOk)
					.addContainerGap())
		);
		setLayout(groupLayout);
	}

	public JTextField getTextField() {
		return textField;
	}
}
