/*
 *  This file is part of Buttermilk
 *  Copyright 2011-2014 David R. Smith All Rights Reserved.
 *
 */
package com.cryptoregistry;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class Assertions extends MapData {
	
	List<String> dataRefs;

	public Assertions() {
		super();
		dataRefs = new ArrayList<String>();
	}

	public Assertions(String uuid) {
		super(uuid);
		dataRefs = new ArrayList<String>();
	}

	public Assertions(String uuid, Map<String, String> in) {
		super(uuid, in);
		dataRefs = new ArrayList<String>();
	}
	
	public Assertions(String uuid, Map<String, String> in, List<String> dataRefs) {
		super(uuid, in);
		this.dataRefs = dataRefs;
	}

}
