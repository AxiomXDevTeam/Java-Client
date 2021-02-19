package net.axiomx.utils;

public class Utils {
	
	public final static char COMMA = ',';
	
	public static String getMessage(String s) {
		int end = s.indexOf(':');
		return end < 0 ? null : s.substring(0, end);
	}
}
