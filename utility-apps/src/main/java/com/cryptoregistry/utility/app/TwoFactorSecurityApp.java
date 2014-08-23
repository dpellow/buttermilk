package com.cryptoregistry.utility.app;

import java.io.File;
import java.nio.charset.Charset;

import asia.redact.bracket.properties.Properties;

import com.cryptoregistry.client.security.TwoFactorSecurityManager;
import com.cryptoregistry.passwords.Password;
import com.cryptoregistry.util.CmdLineParser;
import com.cryptoregistry.util.ShowHelpUtil;
import com.cryptoregistry.util.CmdLineParser.Option;
import com.cryptoregistry.util.CmdLineParser.OptionException;

public class TwoFactorSecurityApp {

	public static final void main(String[] argv) {

		CmdLineParser parser = new CmdLineParser();
		Option<String> fileOpt = parser.addStringOption('f', "configFile");
		Option<Boolean> initKeysOpt = parser.addBooleanOption('i', "initialize");

		try {
			parser.parse(argv);
		} catch (OptionException e) {
			e.printStackTrace();
		}

		String propertiesFile = parser.getOptionValue(fileOpt, null);
		Boolean init = parser.getOptionValue(initKeysOpt, false);

		if (propertiesFile == null & init.booleanValue() == true) {
			ShowHelpUtil.showHelp("/twofactor-help-message.txt");
		}

		if (propertiesFile == null) {
			ShowHelpUtil.showHelp("/twofactor-help-message.txt");
			return;
		}

		File f = new File(propertiesFile);
		if (!f.exists()) {
			System.err.println("File is not readable or does not exist");
			return;
		}

		// load the properties from the file
		Properties props = Properties.Factory.getInstance(f,
				Charset.forName("UTF-8"));

		// check for the removable disk
		TwoFactorSecurityManager manager = new TwoFactorSecurityManager(props);
		if (!manager.checkForRemovableDisk()) {
			System.err.println("Please insert your removable disk.");
			return;
		}

		// load the obfuscated password from the location specified in the
		// properties file
		if (!manager.passwordFileDefined()) {
			System.err
					.println("Password file location does not seem to be defined.");
			return;
		}

		Password pass = manager.loadPassword();

		if (!manager.keysExist() && init.booleanValue() == false) {
			System.err
					.println("Keys don't exist! Run again with the --initialize flag.");
			return;
		}

		if (!manager.keysExist() && init.booleanValue() == true) {
			manager.generateAndSecureKeys(pass);
			System.out.println("Generated and Secured Keys...Successful");
			return;
		}
		
		if (manager.keysExist() && init.booleanValue() == false) {
			if(manager.validate(pass)){
				System.out.println("Configuration is valid. OK.");
			}else{
				System.out.println("Configuration appears invalid.");
			}
			return;
		}
	}

}
