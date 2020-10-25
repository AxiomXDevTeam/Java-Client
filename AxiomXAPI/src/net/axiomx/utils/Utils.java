package net.axiomx.utils;
import net.axiomx.types.MessageType;

public class Utils {
	
	public static String getMessage(String s) {
		int end = s.indexOf(':');
		return end < 0 ? null : s.substring(0, end);
	}
	
	public static MessageType getMessageType(String s) {
		int end = s.indexOf(':');
		
		if(end < 0)
			return null;
		
		String out = s.substring(0, end);
		
		for(MessageType t : MessageType.values()) {
			if(t.toString().equalsIgnoreCase(out))
				return t;
		}
		return null;
	}
}
