package net.axiomx.examples;

import java.io.IOException;
import java.net.UnknownHostException;
import java.util.List;

import net.axiomx.client.AClient;
import net.axiomx.parsers.HistOptDataReq;
import net.axiomx.types.HistoricalDataType;

public class HistDataExample {
	
	public static void main(String [] args) throws UnknownHostException, IOException, InterruptedException {
		AClient client = new AClient("username", "password"); //Connects to the API Server
		
		new HistOptDataReq(); //Registers the wrapper.
		
		Thread.sleep(4000); //Wait for the client to connect
		
		List<String> data = client.getHistOptData("SPY", "20201030", "P", 350.0, HistoricalDataType.BID);
		
		for(String s : data)
			System.out.println(s);
		
		client.disconnect(); //Closes the API connection
	}
}