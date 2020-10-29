# Listing 


#### In the context of Options, we refer to listing is the means of retrieving the universe of a specific trait of an options contract.



### Expirations
Where the parameter is the underlying symbol. This method returns all recorded expired & current expirations.

```java
  client.getAllExps("SPY");
```

### Strikes
Where the first parameter is the underlying symbol and the latter is the expiration date in yyyyMMdd. This returns strikes for puts and calls at that expiration. There is no garuentee that each put strike, there will be a matching call strike and vice-versa.
```java
  client.getAllStrikes("SPY", "20201012");
```