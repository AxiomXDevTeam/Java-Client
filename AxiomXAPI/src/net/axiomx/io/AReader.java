package net.axiomx.io;

import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;

public class AReader {
	
	private InputStream in;
	private byte [] data;
	private int len;
	
	public AReader(InputStream i) {
		in = i;
	}
	
	public byte [] read() throws IOException {
		
		data = new byte[1024];
		len = in.read(data);
		return data;
	}
	
	public byte [] readNByteS(int i) throws IOException {
		return in.readNBytes(i);
	}
	
	public int available() throws IOException {
		return in.available();
	}
	
	public int getLastLen() {
		return len;
	}
	
	
	
}
