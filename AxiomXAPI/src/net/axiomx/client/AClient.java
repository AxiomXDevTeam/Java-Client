package net.axiomx.client;
/* Copyright © 2020, AxiomX Technologies and/or its affiliates. All rights reserved.
* AxiomX Technologies PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
* 
*/
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.TreeMap;

import net.axiomx.types.HistoricalDataType;
import net.axiomx.types.MessageParser;
import net.axiomx.types.MessageType;
import net.axiomx.utils.Utils;

/**
 * <p> Here is a means of accessing AxiomX data. Any request will wait for
 * callbacks before returning any information. If you wish to send concurrent
 * requests, re-implement this class.
 *
 * 
 * 
 * @author Bailey Danseglio
 * @apiNote Editing this class will result in errors if you aren't careful
 * @version 1.9.4
 * 
 */
public class AClient {
	private Socket s;
	private BufferedReader in;
	private PrintWriter out;
	private int reqId;
	private TreeMap<Integer, Object> data = new TreeMap<Integer, Object>();
	private ArrayList<Integer> complete = new ArrayList<Integer>();
	private static TreeMap<MessageType, MessageParser> wrappers = new TreeMap<>();
	
	/**
	 *
	 * Connects to the AxiomX data farm.
	 * 
	 * @param user (This has no effect but should not be left empty)
	 * @param pass The beta product key
	 * @throws UnknownHostException
	 * @throws IOException
	 * @throws InterruptedException
	 */
	public AClient(String user, String pass) throws UnknownHostException, IOException, InterruptedException {
		s = new Socket("farm.axiomx.net", 5050);
		in = new BufferedReader(new InputStreamReader(s.getInputStream()));
		out = new PrintWriter(s.getOutputStream());
		
		Thread.sleep(1000);
		
		out.println(MessageType.CREDENTIALS + ":" + user + "," + pass);
		out.flush();
		Thread.sleep(1000);
		startMessageProcessing();
	}
	
	/**
	 * Register a MessageParser for a message type
	 * @param s Call the MessageType.toString() method
	 * @param p The message parser implementation
	 */
	public static void registerParser(MessageType s, MessageParser p) {
		wrappers.put(s, p);
	}
	
	/**
	 * A thread to listen for callbacks
	 */
	public void startMessageProcessing() {
	new Thread() {
		public void run() {
				try {
					while(s.isConnected()) {
						String msg = in.readLine();
						if(msg == null) {
							System.err.println("Invalid credentials"); return;
						}
						processMessage(msg);
					}
				} catch (IOException e) {
					System.err.println("Lost connection to AxiomX");
				}
		}}.start();
	}
	/**
	 * Decodes and parses callbacks
	 * from the data farm.
	 * @param msg The coded message
	 */
	private void processMessage(String msg) {
		int req = Integer.valueOf(msg.substring(msg.indexOf(':') + 2, msg.indexOf(',')));
		MessageType type = Utils.getMessageType(msg);
		String [] args = msg.substring(msg.indexOf(',') + 1).split(",");
		MessageParser p = wrappers.get(type);
		
		if(p != null)
			data.put(req, p.onCallback(type, req, args));		
		
		if(type.equals(MessageType.HIST_OPT_DATA_END)) 
			complete.add(req);
		
		if(type.equals(MessageType.ALL_EXPS)) 
			data.put(req, Arrays.asList(msg.substring(msg.indexOf(',') + 1).split(",")));
		
		if(type.equals(MessageType.ALL_STRIKES)) 
			data.put(req, Arrays.asList(msg.substring(msg.indexOf(',') + 1).split(",")));	
	}
	
	/**
	 * Sends a market data request
	 * @param e The type of data
	 * @param s The reqiest parameters
	 */
	private void sendReq(MessageType e, String s) {
		out.println(e + ":" + s);
		out.flush();
	}
	
	/**
	 * Returns all Bars available on one
	 * minute intervals.
	 * 
	 * @param sym Underlying symbol
	 * @param exp Expiration date
	 * @param right "Put" for put, "Call" for call
	 * @param strike Strike price
	 * @param type "Trades" for volumes, "midpoint" for mark prices.
	 * @return Bars on 1 minute intervals for the given period
	 */
	@SuppressWarnings("unchecked")
	public ArrayList<String> getHistOptData(String sym, String exp, String right, double strike, String type) {
		int req = reqId++;
		
		sendReq(MessageType.HIST_OPT_DATA_REQ, req + "," + sym + "," + exp + "," + right + "," + Double.valueOf(strike) + "," + type + "," + null + "," + -1); // 7 milliseconds
		
		while(!complete.contains(req)) 
			try { Thread.sleep(1); } catch (InterruptedException e) { e.printStackTrace(); }
		
		return (ArrayList<String>) data.get(req);
	}
	
	public ArrayList<String> getHistOptData(String sym, String exp, String right, double strike, HistoricalDataType type){
		return getHistOptData(sym, exp, right, strike, type.toString());
	}
	
	/**
	 * Retrieves all expiration dates
	 * including expired. 
	 * @param sym The underlying symbol
	 * @return Expiration dates
	 */
	@SuppressWarnings("unchecked")
	public List<String> getAllExps(String sym) {
		int req = reqId++;
		
		sendReq(MessageType.ALL_EXPS, req + "," + sym);
		while(!data.containsKey(req)) 
			try { Thread.sleep(1); } catch (InterruptedException e) { e.printStackTrace(); }
		
		return (List<String>) data.get(req);
	}
	
	/** Retrieves all strike
	 *  prices on a given expiration date
	 * 
	 * @param sym The underlying symbol
	 * @param exp The expiration date
	 * @return All strikes prices on the date.
	 */
	@SuppressWarnings("unchecked")
	public ArrayList<Double> getAllStrikes(String sym, String exp) {
		int req = reqId++;
		
		sendReq(MessageType.ALL_STRIKES, req + "," + sym + "," + exp);
		while(!data.containsKey(req)) 
			try { Thread.sleep(1); } catch (InterruptedException e) { e.printStackTrace(); }
		
		List<String> temp = (List<String>) data.get(req);
		ArrayList<Double> out = new ArrayList<>(temp.size());
		
		for(String s : temp) 
			 out.add(Double.valueOf(s));
		
		return out;
	}
	
	/**
	 * Closes the API connection
	 * @throws IOException
	 */
	public void disconnect() throws IOException {
		s.close();
		in.close();
		out.close();
	}
}