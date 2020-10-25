# 3-Full-Example

#### The aforementioned lines of code are part of this method. 



```java
	public static void main(String [] args) {
		AClient client = new AClient("username", "password"); //Connects to the API Server
		
		new HistOptDataReq(); //Registers the wrapper.
		
		Thread.sleep(4000); //Wait for the client to connect

		List<String> data = client.getHistOptData("SPY", "20201023", "P", 350.0, HistoricalDataType.BID);

		for(String s : data)
		     System.out.println(s);
	}
```



This prints out line for line each bid on 1 minute intervals for the entire recorded lifespan of the option with a strike price of 350.0, 