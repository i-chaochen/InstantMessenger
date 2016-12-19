/**
 * tool class, included static tool's method
 */
package com.im.common;

import java.text.SimpleDateFormat;
import java.util.Date;


public class Utils 

{
	//formating the time as HH:mm:ss,SSS
	public static SimpleDateFormat format = new SimpleDateFormat("HH:mm:ss,SSS");
	
	public static String formatDate(Date date)
	{
		return format.format(date);
	}
	
	// is null String or not
	public static boolean isNullString(String source)
	{
		return source == null || source.isEmpty();
	}
	
	//split String
	public static String[] splitString(String regex, String source)
	{
		if(source == null || source.isEmpty())
			{
			return new String[0];
			}
		return source.split(regex);
	}
	
	
}
