/*
 *  This file is part of Buttermilk
 *  Copyright 2011-2014 David R. Smith All Rights Reserved.
 *
 */
package com.cryptoregistry.utility.app;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;

import com.cryptoregistry.util.CmdLineParser;
import com.cryptoregistry.util.CmdLineParser.Option;
import com.cryptoregistry.util.CmdLineParser.OptionException;
import com.cryptoregistry.util.FileUtil;
import com.cryptoregistry.util.ShowHelpUtil;
import com.cryptoregistry.util.entropy.TresBiEntropy;
import com.cryptoregistry.util.entropy.TresBiEntropy.Result;

public class BiEntropy {

	public static final void main(String[] argv) {
		
		CmdLineParser parser = new CmdLineParser();
		Option<String> fileOpt = parser.addStringOption('f', "file");
		Option<String> argOpt = parser.addStringOption('a', "arg");
		Option<String> charsetOpt = parser.addStringOption('c', "charset");
		
		try {
			parser.parse(argv);
			
			String filePath = parser.getOptionValue(fileOpt);
			String arg = parser.getOptionValue(argOpt);
			
			String cs = parser.getOptionValue(charsetOpt);
			Charset charset = null;
			if(cs == null) {
				charset = Charset.defaultCharset();
			}else {
				charset = Charset.forName(cs);
			}
			
			if(filePath != null) {
				// process file
				String s = FileUtil.readFile(filePath, charset);
				TresBiEntropy bi = new TresBiEntropy(s.getBytes());
				Result res = bi.calc();
				System.out.println(res.toJSON());
			}else if(arg != null){
				// process arg
				if(arg.length() ==1){
					com.cryptoregistry.util.entropy.BiEntropy bi = new com.cryptoregistry.util.entropy.BiEntropy(arg.getBytes()[0]);
					com.cryptoregistry.util.entropy.BiEntropy.Result res = bi.calc();
					System.out.println(res.toJSON());
					return;
				}
				TresBiEntropy bi = new TresBiEntropy(arg.getBytes());
				Result res = bi.calc();
				System.out.println(res.toJSON());
			}else{
				ShowHelpUtil.showHelp("/bientropy-help-message.txt");
			}
		} catch (OptionException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
