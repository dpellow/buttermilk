package com.cryptoregistry.utility.app;

import com.cryptoregistry.util.CmdLineParser;
import com.cryptoregistry.util.CmdLineParser.Option;
import com.cryptoregistry.util.CmdLineParser.OptionException;
import com.cryptoregistry.util.ShowHelpUtil;

public class ButtermilkApp {

	CmdLineParser parser;
	
	private void backup(String [] args){
		
	}

	private void contact(String [] args){
		
	}
	
	public static final void main(String[] argv) {

		if (argv.length == 0) {
			showHelp();
			return;
		}
		
		ButtermilkApp app = new ButtermilkApp();
		
		String anchor = argv[0];
		String[] parameters = new String[argv.length - 1];
		System.arraycopy(argv, 0, parameters, 0, argv.length);
		switch (anchor) {

			case "backup": {
				app.backup(parameters);
				break;
			}
			case "contact": {
				app.contact(parameters);
				break;
			}
			case "datastore": {
	
				break;
			}
			case "export": {
	
				break;
			}
			case "import": {
	
				break;
			}
			case "key": {
	
				break;
			}
			case "list": {
	
				break;
			}
			case "password": {
	
				break;
			}
			case "sign": {
	
				break;
			}
			case "validate": {
	
				break;
			}
			default:
				error("Unknown anchor: " + anchor);
			}
	}

	private static final void showHelp() {
		ShowHelpUtil.help("/buttermilk-help-message.txt");
	}

	private static final void error(String txt) {
		System.err.println(txt);
	}
}
