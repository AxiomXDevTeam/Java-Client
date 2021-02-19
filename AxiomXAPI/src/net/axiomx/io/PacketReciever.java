package net.axiomx.io;

import java.io.IOException;

public class PacketReciever {
	
	private AReader in;
	private int lastSize;
	private byte [] buffer;
	private volatile boolean looking;
	
	public PacketReciever(AReader i) {
		in = i;
	}

	public byte [] read(int i) throws IOException {
		while(looking);
		
		looking = true;
		buffer = new byte[i];	
		while(in.available() <= i);
		
		buffer = in.readNByteS(i);
		lastSize = i;
		looking = false;
		return buffer;
	}
	
	public int getSize() {
		return lastSize;
	}

	public boolean looking() {
		return looking;
	}
}