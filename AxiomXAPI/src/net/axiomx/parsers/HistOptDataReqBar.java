package net.axiomx.parsers;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import net.axiomx.client.AClient;
import net.axiomx.compression.RDG;
import net.axiomx.types.Bar;
import net.axiomx.types.HistoricalDataType;
import net.axiomx.types.MessageParser;
import net.axiomx.types.MessageType;

public class HistOptDataReqBar implements MessageParser {
	private static HashMap<Integer, List<Bar>> ivls = new HashMap<>(32);

	private String [] temp;
	
	public HistOptDataReqBar() {
		AClient.registerParser(MessageType.HIST_OPT_DATA_REQ, this);
	}
	
	@Override
	public Object onCallback(HistoricalDataType type, int req, int dur, List<String> args) {
		List<Bar> out;
		
		String date = args.get(0).substring(2);
		
		if(!ivls.containsKey(req))  //First Item
			out = new ArrayList<>(405 * dur + 1);
		 else 
			out = ivls.get(req);
		
			if(type.equals(HistoricalDataType.OHLC)) 
				out = convertOHLC(args, date, out);
			 else if(type.equals(HistoricalDataType.OPEN_INTEREST)) 
				out =convertOI(args, out);
			 else if(type.equals(HistoricalDataType.VOLUME)) 
				out = convertVol(args, date, out);
			 else 
				out = convertAskOrBid(args, date, out);
			
		ivls.put(req, out);
		return out;
	}

	@Override
	public Object onCallback(MessageType type, int orderId, String[] args) {
		return null;
	}
	
	public List<Bar> convertAskOrBid(List<String> args, String date, List<Bar> in) {
		Bar b;
		int hour = 9;
		int min = 31;
		temp = args.get(0).split(",");
		
		if(temp.length < HistoricalDataType.ASK.len()) //Given time
			for(String s : args) {
			//	System.out.println(s);
				temp = s.split(",");
				if(temp[0].contains(RDG.INFO)) {
					date = temp[0].substring(2);
					continue;
				}
				b = new Bar();
				b.setDate(date);
				b.setTime(Integer.valueOf(temp[0]));
				b.setCount(Integer.valueOf(temp[1]));
				b.setOpen(Double.valueOf(temp[2]));
				in.add(b);
			}
		else //We imply time
			for(String s : args) {
				temp = s.split(",");
				b = new Bar();
				b.setDate(date);
				b.setTime(hour * 100 + min++);
				if(min == 60) {
					min = 0;
					hour++;
				}
				b.setCount(Integer.valueOf(temp[0]));
				b.setOpen(Double.valueOf(temp[1]));
				in.add(b);
			}
		return in;
	}
	
	
	public List<Bar> convertOHLC(List<String> args, String date, List<Bar> in) {
		Bar b;
		int hour = 9;
		int min = 31;
		
		if(temp.length < HistoricalDataType.OHLC.len()) //Given time
			for(String s : args) {
				temp = s.split(",");
				b = new Bar();
				b.setDate(date);
				b.setTime(Integer.valueOf(temp[0]));
				b.setOpen(Integer.valueOf(temp[1]));
				b.setHigh(Double.valueOf(temp[2]));
				b.setLow(Double.valueOf(temp[3]));
				b.setClose(Double.valueOf(temp[4]));
				in.add(b);
			}
		else //We imply time
			for(String s : args) {
				temp = s.split(",");
				b = new Bar();
				b.setDate(date);
				b.setTime(hour * 100 + min++);
				if(min == 60) {
					min = 0;
					hour++;
				}
				b.setOpen(Integer.valueOf(temp[0]));
				b.setHigh(Double.valueOf(temp[1]));
				b.setLow(Double.valueOf(temp[2]));
				b.setClose(Double.valueOf(temp[3]));
				in.add(b);
			}
		return in;
	}
	
	public List<Bar> convertVol(List<String> args, String date, List<Bar> in) {
		Bar b;
		int hour = 9;
		int min = 31;
		temp = args.get(0).split(",");
		
		if(temp.length < HistoricalDataType.ASK.len()) //Given time
			for(String s : args) {
			//	System.out.println(s);
				temp = s.split(",");
				if(temp[0].contains(RDG.INFO)) {
					date = temp[0].substring(2);
					continue;
				}
				b = new Bar();
				b.setDate(date);
				b.setTime(Integer.valueOf(temp[0]));
				b.setVolume(Integer.valueOf(temp[1]));
				in.add(b);
			}
		else //We imply time
			for(String s : args) {
				temp = s.split(",");
				b = new Bar();
				b.setDate(date);
				b.setTime(hour * 100 + min++);
				if(min == 60) {
					min = 0;
					hour++;
				}
				b.setVolume(Integer.valueOf(temp[0]));
				in.add(b);
			}
		return in;
	}
	
	public List<Bar> convertOI(List<String> args, List<Bar> in) {
		Bar b = new Bar();
		if(temp.length < HistoricalDataType.VOLUME.len()) //Given time
			for(String s : args) {
				temp = s.split(",");
				b.setDate(temp[0]);
				b.setCount(Integer.valueOf(temp[1]));
				in.add(b);
			}
		return in;
	}
}