package net.axiomx.types;

public class Bar {
	
	private double open, high, low, close, weightedPrice;
	private int count, volume;
	private String date, time;
	
	public Bar(String date, String time, double open, double high, double low, double close, int volume, int count, double weightedPrice) {
		this.date = date;
		this.time = time;
		this.open = open;
		this.high = high;
		this.low = low;
		this.close = close;
		this.volume = volume;
		this.count = count;
	}

	public int count() {
		return count;
	}

	public void setCount(int count) {
		this.count = count;
	}

	public double getOpen() {
		return open;
	}

	public void setOpen(double open) {
		this.open = open;
	}

	public double high() {
		return high;
	}

	public void setHigh(double high) {
		this.high = high;
	}

	public String date() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}

	public double low() {
		return low;
	}

	public void setLow(double low) {
		this.low = low;
	}

	public int volume() {
		return volume;
	}

	public void setVolume(int volume) {
		this.volume = volume;
	}

	public double close() {
		return close;
	}

	public void setClose(double close) {
		this.close = close;
	}

	public String time() {
		return time;
	}

	public void setTime(String time) {
		this.time = time;
	}

	public double weightedPrice() {
		return weightedPrice;
	}

	public void setWeightedPrice(double weightedPrice) {
		this.weightedPrice = weightedPrice;
	}
	
	public String toString() {
		return date + " " + time + " " + open + " " + high + " " + low + " " + close + " " + volume + " " + count + " " + weightedPrice;
	}
}
