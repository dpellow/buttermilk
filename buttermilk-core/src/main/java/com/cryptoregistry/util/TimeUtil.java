/*
 *  This file is part of Buttermilk
 *  Copyright 2011-2014 David R. Smith All Rights Reserved.
 *
 */
package com.cryptoregistry.util;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**<pre>
 * Used for ISO 8601 formatting, which is our standard for date-time values
 *
 * </pre>
 * @author Dave
 *
 */
public class TimeUtil {
	
	private static Lock lock = new ReentrantLock();

	public static String format(Date date) {
		return getDateFormat().format(date);
	}
	/**
	 * Returns a formatter for ISO 8601 format as described at http://www.cl.cam.ac.uk/~mgk25/iso-time.html
	 * @return
	 */
	public static final DateFormat getDateFormat() {
		  lock.lock(); 
		     try {
		    	 TimeZone tz = TimeZone.getTimeZone("UTC");
		 		 DateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssZ");
		 		 df.setTimeZone(tz);
		 		 return df;
		     } finally {
		       lock.unlock();
		     }
	}
	
	public static final Date getISO8601FormatDate(String in) {
		 lock.lock(); 
	     try {
	    	 try {
	 			return getDateFormat().parse(in);
	 		} catch (ParseException e) {
	 			throw new RuntimeException("Date failed to parse as ISO 8601: "+in);
	 		}
	     } finally {
	       lock.unlock();
	     }
	}
	
	public static final String now() {
		 lock.lock(); 
	     try {
	    	 return getDateFormat().format(new Date());
	     } finally {
	       lock.unlock();
	     }
	}

}
