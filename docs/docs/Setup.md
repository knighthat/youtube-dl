---
sidebar_position: 3
slug: setup
---

# Setup

There are several additional steps to take care
before implementing this wrapper to your project.


## Default

By default, this wrapper will execute command with **python** and **youtube-dl** from your `PATH`

:::tip
Invoke `YoutubeDL.init()` will perform checks on both **python** and **youtube-dl**
in terms of availability and version. This will ensure that no surprise error
occurs during runtime.
:::

## Custom location

If you happen to install **python** or **youtube-dl** at another location. You can use

```java
try {
  YoutubeDL.init("/path/to/python", "/path/to/youtube-dl")
} catch (IOException e) {
  // Error during command execution
} catch (UnsupportedVersionException e) {
  // Either version of python or youtube-dl is
  // older than the required version to run.
}
```

It will reset executable paths and verify both **python** and **youtube-dl** executables.

