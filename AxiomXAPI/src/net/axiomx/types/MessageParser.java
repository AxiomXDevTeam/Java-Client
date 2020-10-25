package net.axiomx.types;

public interface MessageParser { //implements Comparable<MessageParser> 
	
	/**
	 * 
	 * @param type
	 * @param orderId
	 * @param args
	 * @return
	 */
	public Object onCallback(MessageType type, int orderId, String [] args);
}
