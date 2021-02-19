package net.axiomx.types;

public enum HistoricalDataType {
	
	VOLUME("VOL", 1),
	ASK("ASK", 2),
	BID("BID", 2),
	OHLC("OHLC", 4),
	OPEN_INTEREST("OI", 2),
	IMPLIED_VOLATILITY("IV", 4);
	
	private final String type;	
	private final int len;
	
	HistoricalDataType(String s, int i) {
		type = s;
		len = i;
	}
	
	public String toString() {
		return type;
	}
	
	public int len() {
		return len;
	}
}
