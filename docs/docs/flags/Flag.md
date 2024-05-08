---
sidebar_position: 1
slug: flag
---

# Flag

`Flag` is an interface, therefore, it can't be instantiated normally. Instead, you can
create a `Flag` by one these **three** means

## Single key/pair flag

```javascript
Flag.key("--output").value("filename.mp4");
```

## Switch (no value argument)

```javascript
Flag.noValue("--simulate");
```

## Chaining (multiple key/value arguments)

This method requires `build()` to finalize the chain.

```javascript
Flag.chain()
    .key("--output").value("filename.mp4")
    .key("--simulate").value()
    .build();
```

> Please note that `build()` is only accessible when both key and value are provided.\
> _Empty value is **no value switch**._