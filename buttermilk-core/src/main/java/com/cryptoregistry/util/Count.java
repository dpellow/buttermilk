/*
 *  This file is part of Buttermilk(TM) 
 *  Copyright 2013 David R. Smith for cryptoregistry.com
 *
 */
package com.cryptoregistry.util;

/**<pre>
 * Helper class used in parsing Handles.
 *
 * </pre>
 * @author Dave
 *
 */
public class Count {

	final int count;
	final CountType type;
	
	public Count(CountType type, int count) {
		this.type = type;
		this.count = count;
	}

	public int getCount() {
		return count;
	}

	public CountType getType() {
		return type;
	}
	
	public static Count findMax(Count... vals) {
		   int max = Integer.MIN_VALUE;
		   Count maxCount = null;

		   for (Count count : vals) {
		      if (count.getCount() > max) {
		    	  max = count.getCount();
		    	  maxCount = count;
		      }
		   }

		   return maxCount;
	}
}
