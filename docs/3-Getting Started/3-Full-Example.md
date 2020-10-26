# 3 Full Example

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

We request "BID" 1 minute intervals for the entire recorded lifespan of the option with a strike price of 350.0. The expected output:

```java
20201012,0931,530,4.58
20201012,0932,130,4.69
20201012,0933,579,4.62
20201012,0934,23,4.65
20201012,0935,297,4.57
20201012,0936,468,4.48
20201012,0937,127,4.43
20201012,0938,69,4.39
20201012,0939,95,4.4
....
20201023,1615,150,4.51
```