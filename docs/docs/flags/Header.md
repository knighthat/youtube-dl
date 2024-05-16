---
sidebar_position: 4
slug: header
---

# Header

`Header` lets you fine-grain your input. For example, provide access code through **Authorization**,
or spook user agent with **User-Agent**. [Learn more](https://developer.mozilla.org/en-US/docs/Web/HTTP/Headers)

## Single field/value header

```java
Header.key("Content-Type").value("application/json");
```

## Chaining (multiple field/value arguments)

```java
Header.chain()
      .key("Content-Type").value("application/json")
      .key("Authorization").value("Bearer YOUR_ACCESS_TOKEN")
      .build();
```

> Please note that `build()` is only accessible when both key and value are provided.