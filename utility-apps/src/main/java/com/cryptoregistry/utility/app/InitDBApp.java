package com.cryptoregistry.utility.app;

import java.io.File;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

import asia.redact.bracket.properties.Properties;
import asia.redact.bracket.properties.mgmt.PropertiesReference;
import asia.redact.bracket.properties.mgmt.ReferenceType;

import com.cryptoregistry.client.security.TwoFactorSecurityManager;
import com.cryptoregistry.client.storage.DataStore;
import com.cryptoregistry.passwords.Password;
import com.cryptoregistry.util.CmdLineParser;
import com.cryptoregistry.util.ShowHelpUtil;
import com.cryptoregistry.util.CmdLineParser.Option;
import com.cryptoregistry.util.CmdLineParser.OptionException;

public class InitDBApp {

	public static final void main(String[] argv) {

		CmdLineParser parser = new CmdLineParser();
		Option<String> fileOpt = parser.addStringOption('f', "configFile");

		try {
			parser.parse(argv);
		} catch (OptionException e) {
			e.printStackTrace();
		}

		String propertiesFile = parser.getOptionValue(fileOpt, null);

		if (propertiesFile == null) {
			ShowHelpUtil.showHelp("/initDB-help-message.txt");
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
		List<PropertiesReference> refs = new ArrayList<PropertiesReference>();
		refs.add(new PropertiesReference(ReferenceType.CLASSLOADED, "/buttermilk.properties"));
		refs.add(new PropertiesReference(ReferenceType.EXTERNAL, propertiesFile));

		if (!manager.keysExist()) {
			System.err
					.println("Keys don't exist! Run twofactor-setup.sh");
			return;
		}
		
		if(manager.validate(pass)){
			DataStore ds = new DataStore(refs,pass);
			if(ds.getDb() != null) {
				System.out.println("Successfuly created the secure database.");
				ds.closeDb();
			}
		}else{
			System.out.println("Security configuration appears invalid.");
		}
		return;
		
	}

}
