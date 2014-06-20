package com.cryptoregistry.util;

import java.util.Iterator;

public interface MapIterator extends Iterator<String> {

	String get(String key);
}
