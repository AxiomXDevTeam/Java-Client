package net.axiomx.parsers;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import net.axiomx.client.AClient;
import net.axiomx.types.HistoricalDataType;
import net.axiomx.types.MessageParser;
import net.axiomx.types.MessageType;

public class HistOptDataReq implements MessageParser {

	private static HashMap<Integer, ArrayList<String>> ivls = new HashMap<>();
	private static int diffSize;
	private static String lastDate;
	private static boolean tempContains;
	
	public HistOptDataReq() {
		AClient.registerParser(MessageType.HIST_OPT_DATA_CB, this);
	}
	
	@Override
	public Object onCallback(MessageType type, int req, String [] args) {
		ArrayList<String> temp;
		String t = "";
		tempContains = ivls.containsKey(req);
			
		for(String s : args)
			t += "," + s;
		
		t = t.substring(1);
		
		if(!tempContains) { //First Item
			temp = new ArrayList<String>(405);
			temp.add(t);
			ivls.put(req, temp);
			lastDate = args[0];
			diffSize = args.length;					
		} else {			 
			temp = (ArrayList<String>) ivls.get(req);
			if(args.length == diffSize) {
				lastDate = args[0];
				temp.add(t);
			} else
				temp.add(lastDate + "," + t);
			ivls.put(req, temp);
		}
		return ivls.get(req);
	}

	@Override
	public Object onCallback(HistoricalDataType type, int orderId, int duration, List<String> args) {
		// TODO Auto-generated method stub
		return null;
	}
}