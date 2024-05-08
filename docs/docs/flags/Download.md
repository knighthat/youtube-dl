---
sidebar_position: 2
slug: download
---

# Download

`Download` flag lets you configure download options such as **_download rate_** and **_number of retries_**.

```javascript
Download.builder();
```

## Retries
> This method can only take **ONE** argument. Any subsequent call overrides the preceding one.

Specify the number of attempts youtube-dl re-execute command before giving up and throw error.

```javascript
Download.builder().retries(10).build();
```

## Download Rate
> This method can only take **ONE** argument. Any subsequent call overrides the preceding one.

Indicate how many bytes worth of data should be downloaded per second.

```javascript
Download.builder().rate(102400).build();
```




