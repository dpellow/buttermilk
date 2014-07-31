/*
 *  This file is part of Buttermilk
 *  Copyright 2011-2014 David R. Smith All Rights Reserved.
 *
 */
package com.cryptoregistry;

import java.util.List;

public abstract class URLResolver {

	protected String url;

	protected URLResolver() {
		super();
	}

	protected URLResolver(String url) {
		super();
		this.url = url;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}
	
	public abstract List<MapData> resolve();
}
