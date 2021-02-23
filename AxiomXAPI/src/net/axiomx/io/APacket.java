package net.axiomx.io;

import net.axiomx.types.MessageType;

public class APacket {
	
	private MessageType msg;
	private byte [] data;
	private int size;
	private int id;
	
	public APacket(MessageType m, int i, int s, byte [] d)  {
		id = i;
		msg = m;
		size = s;
		data = d;
	}
	
	public APacket() {
		
	}
	
	public void id(int i) {
		id = i;
	}
	
	public int id() {
		return id;
	}
	public void msg(MessageType m) {
		msg = m;
	}
	
	public void data(byte [] d) {
		data = d;
	}
	
	public int size() {
		return size;
	}
	
	public byte [] data() {
		return data;
	}

	public MessageType msg() {
		return msg;
	}
}