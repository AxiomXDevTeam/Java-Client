package net.axiomx.types;

import java.util.List;

/**
 * This interface provides a means of handling server 
 * callbacks to the client. Implementations of should
 * register itself as a parser with @see AClient through
 * a registerParser(MessageType s, MessageParser p) call.
 * 
 * 
 * @author Bailey Danseglio
 * @see MessageType
 *
 */
public interface MessageParser { 
	

	/**
	 * 	The method used to make a request will wait for callbacks. This interface provides
	 * a method for parsing and handling callbacks. The object returned is hashed in 
	 * correspondance to its order ID. Note that the object in a table is commonly
	 * of type @see List. For historical data, where there are multiple elements / callbacks
	 * per request, the implementation of this should have an instance @see Map in that
	 * hashes an order ID with a @see List of callbacks. Each time this method is called,
	 * there should be checks to see if the list exists in the @see Map or if one should be
	 * instaniated and added to the @see Map.
	 * 
	 * @param type
	 * @param orderId
	 * @param args
	 * @return
	 */
	public Object onCallback(HistoricalDataType type, int orderId, List<String> args);
	
	
	}
