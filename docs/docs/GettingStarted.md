---
sidebar_position: 1
slug: getting-started
---

# Getting Started

**YoutubeDL Java** is a wrapper of [youtube-dl](https://github.com/ytdl-org/youtube-dl) - A Python program that let you retrieve video's information from various websites.

## Why do you want to use YoutubeDL Java?

It works just like ORM to databases by creating abstract interfaces and force user to certain pattern that minimize chances of 
making mistakes down to the slightest.

A complete `youtube-dl` command can be built by component. Each component serves its only purpose and can be reused in the future.
Furthermore, components can be stacked at the final step.
These features boost the productivity because it reduces the time constructing everything from scratch.

## Snippet

To retrieve information of a video on YouTube in `youtube-dl`:

```shell
youtube-dl \
  --user-agent 'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/123.0.0.0 Safari/537.3' \
  --force-ipv4 \
  --proxy 'https://example.org' \
  --geo-bypass \
  --rate-limit 1024 \
  --retries 5 \
  --add-header 'Referer: https://www.example.com' \
  --add-header 'Authorization: Bearer YOUR_ACCESS_TOKEN' \
  --list-formats \
  'https://www.youtube.com/watch?v=AjUQkkT4WFg'
```

To execute this command in Java, here's what you have to do:

```java
String[] command = {
    "/usr/bin/youtube-dl",
    "--user-agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/123.0.0.0 Safari/537.3",
    "--force-ipv4",
    "--proxy", "https://example.org",
    "--geo-bypass",
    "--rate-limit", "1024"
    "--retries", "5",
    "--add-header", "Referer: https://www.example.com",
    "--add-header", "Authorization: Bearer YOUR_ACCESS_TOKEN",
    "-list-formats",
    "https://www.youtube.com/watch?v=AjUQkkT4WFg"
};


try {
    List<String> outputs = new ArrayList<>();
    Process process = new ProcessBuilder(command).start();
    
    try (
        InputStream outputs = process.getInputStream();
        InputStreamReader reader = new InputStreamReader( outputs );
        BufferedReader bReader = new BufferedReader( reader )
    ) {
      String line;
      while(((line = bReader.readLine()) != null))
        outputs.add(line);
  }
    
  // Handle outputs here
} catch (IOException | InterruptedException e) {
  // Exception handler
}
```

With YoutubeDL Java:

```java
UserAgent userAgent = UserAgent.CHROME_WINDOWS;
```

```java
Network netConfig = Network.builder()
                           .forceIPv4()
                           .proxy("https://example.org")
                           .build();
```

```java
GeoConfig geoConfig = GeoConfig.builder()
                               .bypass()
                               .build();
```

```java
Download downloadConfig = Download.builder()
                                  .rate(1024)
                                  .retries(5)
                                  .build();
```

```java
Header header = (Header) Header.chain()
                               .key("Referer").value("https://www.example.com")
                               .key("Authorization").value("Bearer YOUR_ACCESS_TOKEN")
                               .build();
```

```java
List<Format> formats = YoutubeDL.formats("https://www.youtube.com/watch?v=AjUQkkT4WFg")
                                .userAgent(userAgent)
                                .geoConfig(geoConfig)
                                .headers(header)
                                .flags(netConfig, downloadConfig)
                                .execute()
                                .items();
```

:::tip
Components such as `userAgent`, `netConfig`, `geoConfig`, `downloadConfig`, `header` 
are reusable.
:::
