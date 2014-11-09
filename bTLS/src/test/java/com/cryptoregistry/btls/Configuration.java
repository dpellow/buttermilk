/*
 *  This file is part of Buttermilk
 *  Copyright 2011-2014 David R. Smith All Rights Reserved.
 *
 */
package com.cryptoregistry.btls;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import com.cryptoregistry.c2.key.Curve25519KeyContents;
import com.cryptoregistry.formats.simplereader.JSONC2Reader;
import com.cryptoregistry.passwords.NewPassword;

import asia.redact.bracket.properties.Properties;
import asia.redact.bracket.properties.mgmt.PropertiesReference;
import asia.redact.bracket.properties.mgmt.ReferenceType;

/**
 * temporary
 * 
 * @author Dave
 *
 */
public class Configuration {

	public static final Configuration CONFIG = new Configuration();
	
	protected Properties props;
	
	private Configuration() {
		init();
	}
	
	protected void init() {
		PropertiesReference ref = new PropertiesReference(ReferenceType.CLASSLOADED, "/keys.properties");
		List<PropertiesReference> refs = new ArrayList<PropertiesReference>();
		refs.add(ref);
		props = Properties.Factory.loadReferences(refs);
	}
	
	public Curve25519KeyContents clientKey(){
		File f = new File(props.resolve("client.file"));
		props.deobfuscate("client.password");
		String password = props.get("client.password");
		JSONC2Reader reader = new JSONC2Reader(f,new NewPassword(password.toCharArray()));
		return (Curve25519KeyContents) reader.parse();
	}
	
	public Curve25519KeyContents serverKey(){
		File f = new File(props.resolve("server.file"));
		props.deobfuscate("server.password");
		String password = props.get("server.password");
		JSONC2Reader reader = new JSONC2Reader(f,new NewPassword(password.toCharArray()));
		return (Curve25519KeyContents) reader.parse();
	}

}
