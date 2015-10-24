package com.sgi.utils;

public class OSUtils {

	static String os = System.getProperty("os.name").toLowerCase();

	public static boolean isWindows()
	{
		return (os.indexOf("win") >= 0);
	}
	
	public static boolean isMac()
	{
		return (os.indexOf("win") >= 0);
	}
}
