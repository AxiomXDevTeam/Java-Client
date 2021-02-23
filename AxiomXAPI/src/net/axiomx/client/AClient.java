package net.axiomx.client;
/* Copyright © 2021, AxiomX, its affliates, and subsidiaries. All rights reserved.
* AxiomX PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
*/
import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import org.eclipse.jdt.annotation.NonNull;

import net.axiomx.compression.DecomV1;
import net.axiomx.compression.Decompressor;
import net.axiomx.compression.RDG;
import net.axiomx.io.APacket;
import net.axiomx.io.PacketReciever;
import net.axiomx.types.Bar;
import net.axiomx.types.HistoricalDataType;
import net.axiomx.types.MessageParser;
import net.axiomx.types.MessageType;

/**
 * <p> Class <code> AClient </code> is a means of accessing AxiomX's data. Any request will wait for
 * callbacks before returning any information. If you wish to send concurrent
 * requests, re-implement @seeMessageParser.
 *
 * 
 * @author Bailey Danseglio
 * @apiNote Editing this class will result in errors if you aren't careful
 * @callbacks Waits
 * @see MessageParser
 * @version 2.0.1
 * 
 */
public class AClient {
	private Socket s;
	private PrintWriter out;
	private int reqId;
	private HashMap<Integer, Object> data = new HashMap<>();
	private HashMap<Integer, HistoricalDataType> reqs = new HashMap<>(32);
	private ArrayList<Integer> complete = new ArrayList<Integer>();
	private static HashMap<MessageType, MessageParser> wrappers = new HashMap<>();
	private Decompressor decompress;
	private PacketReciever p;
	
	/**
	 * Connects to an AxiomX data farm. The client will be disconnected unless
	 * the first message is a credential validation using 
	 * <code>MessageType.CREDENTIALS</code> 
	 * 
	 * @param user (This has no effect but should not be left empty)
	 * @param pass The product key
	 * @throws UnknownHostException The host name is invalid
	 * @see MessageType Implementation for interpeting messages.
	 * @throws IOException
	 * @throws InterruptedException
	 */
	public AClient(String user, String pass) throws UnknownHostException, IOException, InterruptedException {
		RDG.init();
		decompress = new DecomV1();
		s = new Socket("farm.axiomxtechnologies.com", 5050);
		s.setReceiveBufferSize(65536 * 10);
		s.setSendBufferSize(65536 * 10);
		p = new PacketReciever(s.getInputStream());
		out = new PrintWriter(s.getOutputStream());
		s.setSoTimeout(10000000);
		Thread.sleep(1000);
		
		out.println(MessageType.CREDENTIALS + ":" + user + "," + pass);
		out.flush();
		Thread.sleep(1000);
		startMessageProcessing();
	}
	
	public AClient(String user, String pass, Decompressor decom) throws UnknownHostException, IOException, InterruptedException {
		RDG.init();
		decompress = decom;
		s = new Socket("farm.axiomxtechnologies.com", 5050);
		s.setReceiveBufferSize(65536 * 10);
		s.setSendBufferSize(65536 * 10);
		p = new PacketReciever(s.getInputStream());
		out = new PrintWriter(s.getOutputStream());
		s.setSoTimeout(10000000);
		Thread.sleep(1000);
		
		out.println(MessageType.CREDENTIALS + ":" + user + "," + pass);
		out.flush();
		Thread.sleep(1000);
		startMessageProcessing();
	}
	/**
	 *  Instaniate AClient without the intent to connect instantly. 
	 *  Use connect() to connect to AxiomX servers.
	 */
	public AClient() {
		decompress = new DecomV1();
		RDG.init();
	}
	
	/**
	 * Connects to the AxiomX data farm.
	 * 
	 * @param user (This has no effect but should not be left empty)
	 * @param pass The beta product key
	 * @throws InterruptedException
	 * @throws UnknownHostException
	 * @throws IOException
	 */
	public void connect(String user, String pass) throws InterruptedException, UnknownHostException, IOException {
		if(s.isConnected())
			throw new RuntimeException("Client is already connected");
		
		s = new Socket("farm.axiomxtechnologies.com", 5050);
		s.setReceiveBufferSize(65536 * 10);
		out = new PrintWriter(s.getOutputStream());
		s.setSoTimeout(10000000); //This is temporary
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
	public static void registerParser(@NonNull MessageType s,@NonNull MessageParser p) {
		wrappers.put(s, p);
	}
	
	/**
	 * A thread to listen for callbacks
	 */
	private void startMessageProcessing() {
	new Thread() {
		public void run() {
				try {
					while(s.isConnected()) {			  	
						processMessage(p.read());
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
	private void processMessage(APacket a) {
		int req = a.id();
		
		//ERRORS
		if(a.msg() == null || a.msg().equals(MessageType.ERROR)) {
			processError(a);
			return;
		}
		
		if(a.msg().equals(MessageType.HIST_OPT_DATA_END)) {
			complete.add(req);
			return;
		}
		
		MessageParser p = wrappers.get(a.msg());
		
		List<String> args = decompress.decompress(a.data(), 0, a.size());
		
		if(p != null)
			data.put(req, p.onCallback(reqs.get(req), req, args));	
		else if(a.msg().equals(MessageType.INFO))
			System.out.println("Info: " + new String(a.data(), StandardCharsets.US_ASCII));
		else	
			data.put(req, Arrays.asList(new String(a.data(), StandardCharsets.US_ASCII).split(",")));
		// The above line of code should be replaced with something more efficient
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
	
	public void processError(APacket a) {
		System.err.println("ERROR(" + a.id() + "): " + new String(a.data(), StandardCharsets.US_ASCII));
	}
	
	/**
	 * Returns all Bars available on one
	 * minute intervals.
	 * 
	 * @param sym Underlying symbol
	 * @param exp Expiration date
	 * @param right "Put" for put, "Call" for call
	 * @param strike Strike price
	 * @param type @see HistoricalDataType values
	 * @return Bars on 1 minute intervals for the given period
	 */
	@SuppressWarnings("unchecked")
	public List<Bar> reqHistOptData(String sym, String exp, String right, double strike, HistoricalDataType type) {
		int req = reqId++;
		reqs.put(req, type);
																			// below is for formatting it correctly
		sendReq(MessageType.HIST_OPT_DATA_REQ, req + "," + sym + "," + exp + "," + Double.valueOf(strike) + "," + right + "," + type + "," + null + "," + -1 + "," + 1); // 7 milliseconds
		
		while(!complete.contains(req)) 
			try { Thread.sleep(1); } catch (InterruptedException e) { e.printStackTrace(); }
		
		return (List<Bar>) data.get(req); 
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
		out.close();
	}
}