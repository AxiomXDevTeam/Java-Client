package net.axiomx.utils;

public class Timer {
	
	private static long start;
	
	public static void start() {
		start = System.currentTimeMillis();
	}

	public static void stop() {
		System.out.println("Time: " + (System.currentTimeMillis() - start));
	}
	
	public static void stop(String s) {
		System.out.println("Time: " + (System.currentTimeMillis() - start) + " for " + s);
	}
}
