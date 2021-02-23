package net.axiomx.examples;

import java.io.IOException;
import java.net.UnknownHostException;
import java.util.List;

import net.axiomx.client.AClient;
import net.axiomx.parsers.HistOptDataReqBar;
import net.axiomx.types.Bar;
import net.axiomx.types.HistoricalDataType;

public class HistDataExample {
	
	public static void main(String [] args) throws UnknownHostException, IOException, InterruptedException {
	
		AClient a = new AClient("demo", "demo"); //connecting 
		
		new HistOptDataReqBar(); //registering the interpreter
		
		Thread.sleep(1000); //waiting to connect
		
		List<Bar> bars = a.reqHistOptData("SPY", "20210219", "C", 385d, HistoricalDataType.VOLUME);
		
		for(Bar k : bars) 
			if(k.time() % 5 == 0)
				System.out.println(k.toStringNoHLC());

		Thread.sleep(3000);
		a.disconnect();
	}
}