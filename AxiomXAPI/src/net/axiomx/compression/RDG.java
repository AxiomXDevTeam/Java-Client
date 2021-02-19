package net.axiomx.compression;

import java.util.ArrayList;
import java.util.List;

/**
 * Reverse Digraph Class
 * 
 * @author Bailey Danseglio
 *
 */
public class RDG {

	private static char[] cKey;
	private static byte[] cVal;
	public static final String INFO = "/-";
	public static final byte NEWLINE = 13;
	public static final byte EMPTY = 15;
	
	public static void init() {
		cKey = new char[16];
		cVal = new byte[256];
		
		for(byte i = 0; i < 10; i++)
			cKey[i] = (char) (i + 48);
	
		cKey[10] = '.';
		cKey[11] = ',';
		cKey[12] = '/';
		cKey[13] = 'n';
		cKey[14] = '-';
		cKey[15] = 'e';
	
		for(int i = 0; i < 256; i++)
			cVal[i] = 15;
		
		for(byte i = 0; i < cKey.length; i++)
			cVal[(byte) cKey[i]] = i;
	}
	
	public static List<String> toStrings(byte [] b) {
		List<String> l = new ArrayList<>(32);
		StringBuilder out = new StringBuilder(16);
		int t, p1, p2;

		for(byte c : b) {
	        	t = c < 0 ? c + 256 : c;       
	    		p2 = t % 16;
	    		p1 = (t - p2) / 16;
	    	  	
	        	if(p1 == RDG.NEWLINE) {
	        		l.add(out.toString());
	        		out = new StringBuilder(16);
	        		continue;
	        	}
	        	out.append(RDG.key((byte) p1));
	        
	        	if(p2 == RDG.NEWLINE) {
	        		l.add(out.toString());
	        		out = new StringBuilder(16);
	        		continue;
	        	}
	        	out.append(RDG.key((byte) p2));
	      
		}
		
		return l;
	}
	
	public static byte value(char c) {
		return cVal[(byte) c];
	}
	
	public static char key(byte b) {
		if(b < 0)
			//return cKey[256 + b];
			return cKey[b];
		else
			return cKey[b];
	}
}