---
tags: [introduction]
---

# Introduction

#### The AxiomX API offers a low latency approach to retrieving market data. This beta test allows the client to access expired & current options for $SPY. We plan to add support for over 2000 tickers in the near future.

## Limitations

 All data is 15-20 minutes delayed. Live data will be functional in the near future. Beta test accounts are limited to 100 requests per minute. The average response time from NY --> NC for a 2-week duration, 1 minute interval request is 300ms. Clients located over 2 timezones away from our servers in New York can expect results to vary. Option expiraries date back to October 12th, 2020. No option data is deleted and there is no current limitation to the duration of a request. It is not possible to login to our API servers through multiple instances or API types. There is no support for intervals other than 1 minute until the next version. 

## Requirements
• Java 8 or higher

• 4 GB of system memory

• A working knowledge of Java

• Download speeds in excess of 10mbps