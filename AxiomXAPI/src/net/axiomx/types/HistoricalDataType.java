package net.axiomx.types;

public enum HistoricalDataType {
	
	VOLUME("VOL"),
	ASK("ASK"),
	BID("BID"),
	BAR("BAR"),
	OPEN_INTEREST("OI");
	
	final String type;	
	
	HistoricalDataType(String s) {
		type = s;
	}
	
	public String toString() {
		return type;
	}
}
