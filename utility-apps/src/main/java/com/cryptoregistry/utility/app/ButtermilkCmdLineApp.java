/*
 *  This file is part of Buttermilk
 *  Copyright 2011-2014 David R. Smith All Rights Reserved.
 *
 */
package com.cryptoregistry.utility.app;

import java.util.Date;

import com.cryptoregistry.client.storage.DataStore;
import com.cryptoregistry.rsa.CryptoFactory;
import com.cryptoregistry.rsa.RSAKeyContents;
import com.cryptoregistry.rsa.RSAKeyMetadata;
import com.cryptoregistry.util.CmdLineParser;
import com.cryptoregistry.util.CmdLineParser.OptionException;
import com.cryptoregistry.util.ShowHelpUtil;
import com.cryptoregistry.util.CmdLineParser.Option;

public class ButtermilkCmdLineApp {
	
	private DataStore ds;
	
	private CmdLineParser parser;
	
	private Option<Boolean> genKeyOpt, statusOpt, listOpt, importOpt, exportOpt,
	signatureOpt, listKeyGenAlgsOpt, listCurveNamesOpt, listNamedParametersOpt, 
	showCustomCurveExampleOpt, showCustomParamExampleOpt;
	
	private Option<String> regHandleOpt, fileOpt, charsetOpt,
	keyOpt, contactOpt, dataOpt, algorithmOpt, handleOpt,
	curveNameOpt, namedParamsOpt;
	
	private Option<Integer> certaintyOpt, publicExponentOpt, strengthOpt;
	
	String commandLine;
	
	public ButtermilkCmdLineApp(DataStore ds){
		this.ds = ds;
	}
	
	public void init(String [] args){
		
		 StringBuilder b = new StringBuilder();
		 for(String s: args){
			 b.append(s);
			 b.append(" ");
		 }
		 b.deleteCharAt(b.length()-1);
		 commandLine = b.toString();
		 
		 parser = new CmdLineParser();
		 genKeyOpt = parser.addBooleanOption('g', "genkey");
		 regHandleOpt = parser.addStringOption('r', "regHandle");
		 fileOpt = parser.addStringOption('f', "file");
		 charsetOpt = parser.addStringOption('c', "charset");
		 statusOpt = parser.addBooleanOption("status");
		 listOpt = parser.addBooleanOption('l', "list");
		 importOpt = parser.addBooleanOption('i', "import");
		 exportOpt = parser.addBooleanOption('e', "export");
		 keyOpt = parser.addStringOption('k', "key");
		 contactOpt = parser.addStringOption("contact");
		 dataOpt = parser.addStringOption("data");
		 signatureOpt = parser.addBooleanOption("signature");
		 algorithmOpt = parser.addStringOption('a', "algorithm");
		 handleOpt = parser.addStringOption('h', "handle");
		 listKeyGenAlgsOpt = parser.addBooleanOption("listKeyGenAlgs");
		 listCurveNamesOpt = parser.addBooleanOption("listCurveNames");
		 listNamedParametersOpt = parser.addBooleanOption("listNamedParameters");
		 showCustomCurveExampleOpt = parser.addBooleanOption("showCustomCurveExample");
		 showCustomParamExampleOpt = parser.addBooleanOption("showCustomParamExample");
		
		// rsa
		 certaintyOpt = parser.addIntegerOption("certainty");
		 publicExponentOpt = parser.addIntegerOption("publicExponent");
		 strengthOpt = parser.addIntegerOption("strength");
		
		// EC
		 curveNameOpt = parser.addStringOption("curveNameAlgs");
		
		// NTRU
		 namedParamsOpt = parser.addStringOption("namedParams");
		
		try {
			parser.parse(args);
		} catch (OptionException e) {
			e.printStackTrace();
		}
	}
	
	public String execute(){
		 
		String output = null;
		
		Date start = new Date();
		try {
			output = _execute();
		}catch(Exception x){
			StackTraceElement [] stes = x.getStackTrace();
			StringBuffer buf = new StringBuffer();
			for(StackTraceElement e : stes){
				buf.append(e.toString());
			}
			output = buf.toString();
		}
		Date end = new Date();
		long duration = end.getTime()-start.getTime();
		return ShowHelpUtil.res(commandLine, output, duration);
		
	}

	private String _execute() {
		
		String cmd = null;
		
		// verbs
		 if(parser.getOptionValue(genKeyOpt, false)) cmd = "KEYGEN";
		 else if(parser.getOptionValue(statusOpt, false)) cmd = "STATUS";
		 else if(parser.getOptionValue(listOpt, false)) cmd = "LIST";
		 else if(parser.getOptionValue(importOpt, false)) cmd = "IMPORT";
		 else if(parser.getOptionValue(exportOpt, false)) cmd = "EXPORT";
		 else if(parser.getOptionValue(signatureOpt, false)) cmd = "SIG";
		 else if(parser.getOptionValue(listKeyGenAlgsOpt, false)) cmd = "LISTALGS";
		 else if(parser.getOptionValue(listCurveNamesOpt, false)) cmd = "LISTCURVES";
		 else if(parser.getOptionValue(listNamedParametersOpt, false)) cmd = "LISTNAMEDPARAMS";
		 else if(parser.getOptionValue(showCustomCurveExampleOpt, false)) cmd = "SHOWCUSTOMCURVEEXAMPLE";
		 else if(parser.getOptionValue(showCustomParamExampleOpt, false)) cmd = "SHOWCUSTOMPARAMEXAMPLE";
		 
		 if(cmd == null) {
			return message(); 
		 }
		 
		 String regHandle = parser.getOptionValue(regHandleOpt, null);
		 if(regHandle == null) regHandle = ds.getRegHandle();
		 
		 System.err.println("command: "+cmd);
		 
		 switch(cmd) {
		 	case "KEYGEN": {
		 		String alg = parser.getOptionValue(algorithmOpt, "none");
		 		if(alg.equals("none")){
		 			return "Please use the -a flag and provide an algorithm";
		 		}
		 		
		 		String handle = parser.getOptionValue(handleOpt, "");
		 		
		 		if(alg.equals("RSA")){
		 			Integer certainty = parser.getOptionValue(certaintyOpt, 100);
		 			Integer publicExponent = parser.getOptionValue(publicExponentOpt, 65537);
		 			Integer strength = parser.getOptionValue(strengthOpt, 2048);
		 			
		 			if(certainty < 10) {
		 				throw new RuntimeException("RSA Primality certainty test count looks too low.");
		 			}
		 			
		 			if((strength % 1024) != 0) {
		 				throw new RuntimeException("RSA key size must be a multiple of 1024");
		 			}
		 			
		 			return genRSAKey(regHandle,handle,certainty,publicExponent,strength);
		 		}
		 	}
		 	default: throw new RuntimeException("Unknown Verb: "+cmd);
		 }
		 
		// return "OK";

	}
	
	public String genRSAKey(String regHandle, String handle, int certainty,int publicExponent,int strength){
		RSAKeyMetadata meta = RSAKeyMetadata.createDefault(handle);
		RSAKeyContents contents = CryptoFactory.INSTANCE.generateKeys(meta,certainty,publicExponent,strength);
		ds.getViews().put(regHandle, contents);
		return handle;
	}

	
	public String message() {
		return ShowHelpUtil.help("/help-message.txt");
	}

}
