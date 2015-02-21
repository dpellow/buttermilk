package com.cryptoregistry.utility.app;

import java.io.IOException;

import com.cryptoregistry.util.CmdLineParser;
import com.cryptoregistry.util.CmdLineParser.Option;
import com.cryptoregistry.util.CmdLineParser.OptionException;


public class ButtermilkApp {

public static final void main(String[] argv) {
		
		CmdLineParser parser = new CmdLineParser();
		Option<String> fileOpt = parser.addStringOption('f', "file");
		Option<String> argOpt = parser.addStringOption('a', "arg");
		Option<String> charsetOpt = parser.addStringOption('c', "charset");
		
		try {
			parser.parse(argv);
			
			
		} catch (OptionException e) {
			e.printStackTrace();
		} 
	}

}
