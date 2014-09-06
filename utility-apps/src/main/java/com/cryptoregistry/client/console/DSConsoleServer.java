package com.cryptoregistry.client.console;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import com.cryptoregistry.client.security.TwoFactorSecurityManager;
import com.cryptoregistry.client.storage.DataStore;
import com.cryptoregistry.passwords.Password;
import com.cryptoregistry.util.CmdLineParser;
import com.cryptoregistry.util.CmdLineParser.Option;
import com.cryptoregistry.util.CmdLineParser.OptionException;
import com.cryptoregistry.util.ShowHelpUtil;

import asia.redact.bracket.properties.Properties;
import asia.redact.bracket.properties.mgmt.PropertiesReference;
import asia.redact.bracket.properties.mgmt.ReferenceType;

public class DSConsoleServer extends DSConsoleFrontEnd {

	public DSConsoleServer(MODE name, DataStore ds) {
		super(name, ds);
	}
	
	public static void main(String [] args){
		System.out.println("Starting up...");
		
		if(args.length == 0) {
			ShowHelpUtil.showHelp("/dsserver-help-message.txt");
			System.exit(2);
		}
		
		CmdLineParser parser = new CmdLineParser();
		Option<String> fileOpt = parser.addStringOption('f', "configFile");
		try {
			parser.parse(args);
		} catch (OptionException e1) {
			e1.printStackTrace();
			return;
		}
		
		List<PropertiesReference> refs = new ArrayList<PropertiesReference>();
		refs.add(new PropertiesReference(ReferenceType.CLASSLOADED, "/buttermilk.properties"));
		
		Collection<String> cl = parser.getOptionValues(fileOpt);
		System.out.println("options: "+cl);
		if(cl.size() == 0) {
			ShowHelpUtil.showHelp("/dsserver-help-message.txt");
			System.exit(2);
		}
		
		Iterator<String> iter = cl.iterator();
		while(iter.hasNext()){
			String i = iter.next();
			System.out.println("option: "+i);
			refs.add(new PropertiesReference(ReferenceType.EXTERNAL, i));
		}

		// load the properties from the file
		Properties props = Properties.Factory.loadReferences(refs);

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

		if (!manager.keysExist()) {
			System.err
					.println("Keys don't exist! Run twofactor-setup.sh");
			return;
		}
		
		Password password = manager.loadPassword();
		DataStore ds;
		if(manager.validate(password)){
			try {
				ds = new DataStore(refs,password);
			}catch(Exception x){
				x.printStackTrace();
				System.out.println("Server could not start due to a problem with the data store initialization.");
				return;
			}
		}else{
			System.out.println("Security configuration appears invalid.");
			return;
		}
			
		try {
			new DSConsoleServer(MODE.buttermilkDB,ds).start();
			System.out.println("Server started.");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}


}
