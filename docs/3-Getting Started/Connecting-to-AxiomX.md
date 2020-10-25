# Connecting-to-AxiomX



Conencting to AxiomX entails instaniating an `AClient` object. The credentials are sent to AxiomX servers to be verified, the client will disconnect if the credentials are invalid. The username can be any extraneous `String` as there are no accounts associated with the beta. The password must be an API product key.

```java
  AClient client = new AClient("username", "passowrd"); 
```



```java
  new HistOptDataReq();
```