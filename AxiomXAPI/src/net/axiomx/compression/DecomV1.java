package net.axiomx.compression;

import java.util.ArrayList;
import java.util.List;


public class DecomV1 implements Decompressor {

	
	@Override
	public List<String> decompress(byte[] l, int len, int start) {
		return RDG.toStrings(l);
	}
	
	public List<String> decompress2(String s) {
		StringBuilder b = new StringBuilder(16);
		List<String> out = new ArrayList<String>(405);
		byte [] k = s.getBytes();
		int t, p1, p2;
		char prev = 'e';
		b.append(RDG.INFO);
		
		for(byte c : k) {
	        t = c < 0 ? c + 256 : c;       
	    	p2 = t % 16;
	    	p1 = (t - p2) / 16;
	    	 
	    	if(prev != 'e' && prev != 'n') {
	    		b.append(prev);
	    		prev = 'e';
	    	}
	    	
	        if(p1 == RDG.NEWLINE || p1 == RDG.EMPTY) {
	        	out.add(b.toString());
	       		b = new StringBuilder(16);
	       		prev = RDG.key((byte) p2);
	       		continue;
	        }
	        	
	        b.append(RDG.key((byte) p1));
	       
	       	if(p2 == RDG.NEWLINE || p2 == RDG.EMPTY) {
	       		out.add(b.toString());
	       		b = new StringBuilder(16);
	       		continue;
	       	}
	       		
	        	
	       	b.append(RDG.key((byte) p2));
	        }
		return out;
	}

	@Override
	public int version() {
		return 1;
	}
	

	public List<String> decompress3(String s) {
		StringBuilder b = new StringBuilder(16);
		List<String> out = new ArrayList<String>(405);
		char [] l = s.toCharArray();
		
		for(int i = 0; i < l.length; i++) {
			
			if(l[i] == 'e'){
				out.add(b.toString());
				b = new StringBuilder(16);
				continue;
			} 
			
			if(l[i] == 'n') {
				if(l[i - 1] == 'n') 
					continue;
				else {
					out.add(b.toString());
					b = new StringBuilder(16);
				}
			} else
				b.append(l[i]);
		}

		return out;
	}
}
