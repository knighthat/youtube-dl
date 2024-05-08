---
sidebar_position: 6
slug: user-agent
---

# User Agent

Tricking server to believe that you send this request from a specific web browser.

`UserAgent` class already provides common user agent (gets updated once in a while).\
You can select them by invoking `UserAgent.<browser>_<platform>`

```javascript
UserAgent.CHROME_WINDOWS;
```

## Custom User Agent

If default values are not what you're after. 
You can specify your own user agent

```javascript
UserAgent.custom("Mozilla/5.0 (Android 14; Mobile; rv:125.0) Gecko/125.0 Firefox/125.0");
```