package net.axiomx.parsers;

import java.util.ArrayList;
import java.util.List;

import net.axiomx.client.AClient;
import net.axiomx.compression.RDG;
import net.axiomx.types.Bar;
import net.axiomx.types.HistoricalDataType;
import net.axiomx.types.MessageParser;
import net.axiomx.types.MessageType;

public class HistOptDataReqBar implements MessageParser {
	
	private String [] temp;
	
	public HistOptDataReqBar() {
		AClient.registerParser(MessageType.HIST_OPT_DATA_REQ, this);
	}
	
	@Override
	public Object onCallback(HistoricalDataType type, int req, List<String> args) {
		if(type == null)
			return null;
		
		if(type.equals(HistoricalDataType.OHLC)) 
			return convertOHLC(args);
		
		if(type.equals(HistoricalDataType.OPEN_INTEREST)) 
			return convertOI(args);
		
		if(type.equals(HistoricalDataType.VOLUME)) 
			return convertVol(args);
		 
			return convertAskOrBid(args); //Must be BID or ASK
	}
	
	public List<Bar> convertAskOrBid(List<String> args) {
		List<Bar> out = new ArrayList<>(args.size());
		boolean partial = false;
		Bar b;
		int min = 9, hour = 31;
		
		temp = args.get(1).split(",");
		String s, date = args.get(0).substring(2);
		
		for(int i = 0; i < args.size(); i++) {
			s = args.get(i);
			temp = s.split(",");
			if(s.contains(RDG.INFO)) {
				min = 9;
				hour = 31;
				date = s.substring(2);
				partial = args.get(i + 1).split(",").length == HistoricalDataType.ASK.len();
				continue;
			}
			b = new Bar();
			b.setDate(date);
			
			if(partial) {
				System.out.println("Partial");
				if(min == 60) {
					min = 0;
					hour++;
				}
				b.setTime(hour * 100 + min++);
				b.setCount(Integer.valueOf(temp[0]));
				b.setOpen(Double.valueOf(temp[1]));
			} else {
			b.setTime(Integer.valueOf(temp[0]));
			b.setCount(Integer.valueOf(temp[1]));
			b.setOpen(Double.valueOf(temp[2]));
			}
			
			out.add(b);
		}
		return out;
	}
	
	public List<Bar> convertOHLC(List<String> args) {
		List<Bar> out = new ArrayList<>(args.size());
		boolean partial = false;
		Bar b;
		int min = 9, hour = 31;
	
		temp = args.get(1).split(",");
		String s, date = args.get(0).substring(2);
		
		for(int i = 0; i < args.size(); i++) {
			s = args.get(i);
			temp = s.split(",");
			if(s.contains(RDG.INFO)) {
				min = 9;
				hour = 31;
				date = s.substring(2);
				partial = args.get(i + 1).split(",").length == HistoricalDataType.OHLC.len();
				continue;
			}
			b = new Bar();
			b.setDate(date);
			
			if(partial) {
				if(min == 60) {
					min = 0;
					hour++;
				}
				b.setTime(hour * 100 + min++);
				b.setOpen(Integer.valueOf(temp[0]));
				b.setHigh(Double.valueOf(temp[1]));
				b.setLow(Double.valueOf(temp[2]));
				b.setClose(Double.valueOf(temp[3]));
			} else {
				b.setTime(Integer.valueOf(temp[0]));
				b.setOpen(Double.valueOf(temp[1]));
				b.setHigh(Double.valueOf(temp[2]));
				b.setLow(Double.valueOf(temp[3]));
				b.setClose(Double.valueOf(temp[4]));
			}
			out.add(b);
		}
		return out;
	}
	
	public List<Bar> convertVol(List<String> args) {
		List<Bar> out = new ArrayList<>(args.size());
		boolean partial = false;
		Bar b;
		int min = 9, hour = 31;
		
		temp = args.get(1).split(",");
		String s, date = args.get(0).substring(2);
		
		for(int i = 0; i < args.size(); i++) {
			s = args.get(i);
			temp = s.split(",");
			if(s.contains(RDG.INFO)) {
				min = 9;
				hour = 31;
				date = s.substring(2);
				partial = args.get(i + 1).split(",").length == HistoricalDataType.VOLUME.len();
				continue;
			}
			b = new Bar();
			b.setDate(date);
			
			if(partial) {
				if(min == 60) {
					min = 0;
					hour++;
				}
				b.setTime(hour * 100 + min++);
				b.setVolume(Integer.valueOf(temp[0]));
			} else {
				b.setTime(Integer.valueOf(temp[0]));
				b.setVolume(Integer.valueOf(temp[1]));
			}
			out.add(b);
		}		
		return out;
	}
	
	public List<Bar> convertOI(List<String> args) {
		Bar b;
		List<Bar> out = new ArrayList<>(args.size());
		
			for(String s : args) {
				b = new Bar();
				temp = s.split(",");
				b.setDate(temp[0]);
				b.setCount(Integer.valueOf(temp[1]));
				out.add(b);
			}
		return out;
	}
}