/*
 *  This file is part of Buttermilk
 *  Copyright 2011-2015 David R. Smith All Rights Reserved.
 *
 */
package com.cryptoregistry.util;

import java.util.Iterator;

/**
 * MapIterators are used to iterate over contents where there are attributes (key value pairs).
 * A distinguishing feature of these data structures is that the underlying data has a handle 
 * (typically a UUID).
 * 
 * @author Dave
 *
 */
public interface MapIterator extends Iterator<String> {

	String getHandle();
	String get(String key);
}
