---
tags: [introduction]
---

# Introduction

 The Theta Data API offers a low latency approach to retrieving market data.

## Limitations

  All data is 15-20 minutes delayed. Live data will be functional in the near future. The average response time from NY --> NC for a 2-week duration, 1 minute interval request is 300ms. Clients located over 2 timezones away from our servers in New York can expect results to vary. It is not possible to login to our API servers through multiple instances or API types. There is no support for intervals lower than 1 minute.

## Requirements
• Java 12

• 4 GB of system memory

• Working knowledge of Java

• Download speeds in excess of 10mbps