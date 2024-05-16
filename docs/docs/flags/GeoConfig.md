---
sidebar_position: 3
slug: geo-config
---

# GeoConfig

`GeoConfig` lets you adjust your geographical to appear differently on the server you're connecting to.

```java
GeoConfig.builder();
```

## Regional Bypass

Faking an X-Forwarded-For HTTP header and let you access region-lock content.

```java
GeoConfig.builder().bypass().build();
```

> It can only be turned on, additional `bypass()` call won't do anything different.

## Country Code
> This method can only take **ONE** argument. Any subsequent call overrides the preceding one.

Pretend to access the server from a different country. (Can be used to access region-lock content)

```java
GeoConfig.builder().countryCode("AU").build();
```

Country code must follow [ISO 3166-1](https://en.wikipedia.org/wiki/ISO_3166-1) format.
