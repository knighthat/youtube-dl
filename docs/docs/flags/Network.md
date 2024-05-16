---
sidebar_position: 5
slug: network
---

# Network

Looking to route your traffic through a proxy? Or reduce waiting time when a connection hangs. `Network` is here to help you

```java
Network.builder();
```

## Proxy
> This method can only take **ONE** argument. Any subsequent call overrides the preceding one.

Route your requests through a proxy server.

```java
Network.builder().proxy("https://example.com").build();
```

> Support multiple schemes such as **HTTP**, **HTTPS**, **SOCK5**

## Timeout
> This method can only take **ONE** argument. Any subsequent call overrides the preceding one.

Increase or decrease waiting time before dropping connection if service is unresponsive.

```java
Network.builder().timeout(5).build();
```

## Source
> This method can only take **ONE** argument. Any subsequent call overrides the preceding one.

Mask sender IP address

```java
Network.builder().source("0.0.0.0").build();
```

## Force IPv4

Make request using IPv4

```java
Network.builder().forceIPv4().build();
```

> It can only be turned on, additional `forceIPv4()` call won't do anything different.

:::danger[Incompatible argument]
Adding `forceIPv6` after this will result in `IllegalArgumentException`
:::

## Force IPv6

Make request using IPv6

```java
Network.builder().forceIPv6().build();
```

> It can only be turned on, additional `forceIPv6()` call won't do anything different.

:::danger[Incompatible argument]
Adding `forceIPv4` after this will result in `IllegalArgumentException`
:::