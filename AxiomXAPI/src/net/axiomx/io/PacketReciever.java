package net.axiomx.io;

import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;

import net.axiomx.types.MessageType;

public class PacketReciever {
	
	public final static byte COLEN = (byte) 58; //58 being the ASCII value for a colen ":"
	private InputStream in;
	
	public PacketReciever(InputStream i) {
		in = i;
	}

	public APacket read() throws IOException {
		int size, id;
		
		byte b;
		byte [] buffer = new byte[4];
		byte c = 0;
		
		for(byte i = 0; i < 4; i++) 
			buffer[i] = (byte) in.read();
		
		size = ByteBuffer.wrap(buffer).getInt();
		buffer = new byte[4];
		
		while((b = (byte) in.read()) != COLEN) //message type
			buffer[c++] = b;
		
		MessageType msg = MessageType.from(new String(buffer, 0, (int) c, StandardCharsets.US_ASCII));
		
		buffer = new byte[4];
		c = 0;
		
		while((b = (byte) in.read()) != COLEN) //OrderId
			buffer[c++] = b;
		
		id = ByteBuffer.wrap(buffer, 0, c).getInt();

		buffer = new byte[size];
		
		for(int i = 0; i < size; i++) 
			buffer [i] = (byte) in.read();
		
		return new APacket(msg, id, size, buffer);
	}
}