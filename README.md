<h1 align="center">
  YoutubeDL Java Wrapper
</h1>

`youtube-dl` main selling points is it allows you to download media from various websites.There are more, such as, extract information of a video, get framengs (media quality) and merge into one file, etc.

# YOUTUBE-DL WRAPPER

This wrapper to youtube-dl is similar to ORM to databases, it provides an abstraction to the command and let user use objects to interact with the command instead of wring the whole command.

Here is a snippet of raw command:

```sh
youtube-dl --user-agent 'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/123.0.0.0 Safari/537.3' \
  --force-ipv4 \
  --proxy 'https://example.org' \
  --list-formats \
  'https://www.youtube.com/watch?v=AjUQkkT4WFg'
```
Translated version:

```java
  GeoConfig geoBypass = GeoConfig.builder()
                                 .bypass(true)
                                 .countryCode("US")
                                 .build();
  NetworkConfig netConfig = NetworkConfig.builder()
                                         .forceV4(true)
                                         .proxy("https://example.org")
                                         .build();

  List<Format> formats = YoutubeDL.formats("https://www.youtube.com/watch?v=AjUQkkT4WFg")
                                  .flags(geoBypass, netConfig)
                                  .userAgent(UserAgent.CHROME_WINDOWS)
                                  .execute()
                                  .items();
```

It may look a bit much. But when you deal with a longer command or when you have some default configuration you can easily save store it for later use.

# License

This project inherits license from [youtube-dl](https://github.com/ytdl-org/youtube-dl/blob/master/LICENSE) which is [**Unlicense License**](https://unlicense.org/)

