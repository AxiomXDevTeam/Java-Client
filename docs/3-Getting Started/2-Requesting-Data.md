# Requesting Data


Beta 1.0.2 does not support object-based return types without reimplementing `AClient`. Expect to see additional methods in the next version to return a List of type **IVL**.

```java
  List<String> data;
```

Calling `getHistOptData()` on the `AClient` object triggers a historical data request. `AClient` will wait for callbacks until a response has been recieved. Callbacks are checked for every millisecond, if this does not satisfy your needs, consider using the source and changing the `Thread.sleep()` parameter(s).

```java
  data = client.getHistOptData("SPY", "20201023", "P", 350.0, HistoricalDataType.BID);
```

