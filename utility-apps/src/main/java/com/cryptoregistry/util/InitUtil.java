/*
 *  This file is part of Buttermilk(TM) 
 *  Copyright 2013-2014 David R. Smith for cryptoregistry.com
 *
 */
package com.cryptoregistry.util;

import java.io.File;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.log4j.PropertyConfigurator;

import asia.redact.bracket.properties.Properties;
import asia.redact.bracket.properties.mgmt.PropertiesReference;
import asia.redact.bracket.properties.mgmt.ReferenceType;

/**
 * Bits related to startup
 * 
 * @author Dave
 *
 */
public class InitUtil {
	
	public static String URI = "config.properties";
	public static String log4j = "log4j.properties";
	
	public static String currentUser() {
		return (String) System.getProperties().get("user.name");
	}
	
	public static void setupHTTPProxy(Properties props){
		boolean on = Boolean.valueOf(props.get("http.proxy.on"));
		if(!on) return;
		String host = props.get("http.proxyHost");
		String port = props.get("http.proxyPort");
		String sslHost = props.get("https.proxyHost");
		String sslPort = props.get("https.proxyPort");
		System.setProperty("http.proxyHost", host);
		System.setProperty("http.proxyPort", port);
		System.setProperty("https.proxyHost", sslHost);
		System.setProperty("https.proxyPort", sslPort);
	}

	public static Properties findProps() {

		// base configuration properties loaded from an embedded config file in
		// the app classpath
		String templateProps = "/"+URI;

		// file might be in user.home
		String home = System.getProperty("user.home");
		String adminExternalProps = home + File.separator + URI;

		List<PropertiesReference> refs = new ArrayList<PropertiesReference>();
		refs.add(new PropertiesReference(ReferenceType.CLASSLOADED, templateProps));
		// override
		refs.add(new PropertiesReference(ReferenceType.EXTERNAL, adminExternalProps));

		return Properties.Factory.loadReferences(refs);
	}
	
	/**
	 * Log4j properties can be externalized to user.home folder. Those will be added/override the defaults file in classpath
	 */
	public static void initLog4j() {

		// base configuration properties loaded from an embedded config file in
		// the app classpath
		String templateProps = "/"+log4j;

		// file might be in user.home
		String home = System.getProperty("user.home");
		String adminExternalProps = home + File.separator + log4j;

		List<PropertiesReference> refs = new ArrayList<PropertiesReference>();
		refs.add(new PropertiesReference(ReferenceType.CLASSLOADED, templateProps));
		// override
		refs.add(new PropertiesReference(ReferenceType.EXTERNAL, adminExternalProps));

		Properties props = Properties.Factory.loadReferences(refs);
		java.util.Properties legacy = new java.util.Properties();
		Iterator<String> iter = props.getPropertyMap().keySet().iterator();
		while(iter.hasNext()) {
			String key = iter.next();
			String value= props.get(key);
			legacy.put(key, value);
		}
		
		PropertyConfigurator.configure(legacy);
	}

}
