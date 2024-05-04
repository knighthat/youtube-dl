<h1 align="center">
  YoutubeDL Java Wrapper
</h1>

`youtube-dl` main selling points is it allows you to download media from various websites.There are more, such as, extract information of a video, get framengs (media quality) and merge into one file, etc.

# YOUTUBE-DL WRAPPER

This wrapper to youtube-dl is similar to ORM to databases, it provides an abstraction to the command and let user use objects to interact with the command instead of wring the whole command.

Here is a snippet of raw command:

```sh
youtube-dl \
  --user-agent 'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/123.0.0.0 Safari/537.3' \
  --force-ipv4 \
  --proxy 'https://example.org' \
  --geo-bypass \
  --rate-limit 1024 \
  --retries 5 \
  --add-header "Referer: https://www.example.com" \
  --add-header "Authorization: Bearer YOUR_ACCESS_TOKEN" \
  --list-formats \
  'https://www.youtube.com/watch?v=AjUQkkT4WFg'
```
Translated version:

```java
  UserAgent userAgent = UserAgent.CHROME_WINDOWS;

  NetworkConfig netConfig = NetworkConfig.builder()
                                         .forceIPv4()
                                         .proxy("https://example.org")
                                         .build();
                                    
  GeoConfig geoConfig = GeoConfig.builder()
                                 .bypass()
                                 .build();
                              
  Download downloadConfig = Download.builder()
                                    .rate(1024)
                                    .retries(5)
                                    .build();

  Header header = Header.chain()
                        .key("Referer").value("https://www.example.com")
                        .key("Authorization").value("Bearer YOUR_ACCESS_TOKEN")
                        .build();

  List<Format> formats = YoutubeDL.formats("https://www.youtube.com/watch?v=AjUQkkT4WFg")
                                  .userAgent(userAgent)
                                  .geoConfig(geoConfig)
                                  .headers(header)
                                  .flags(netConfig, downloadConfig)
                                  .execute()
                                  .items();
```

It may look a bit much. But when you deal with a longer command or when you have some default configuration you can easily save store it for later use.

# Installation

## Maven
```xml
<repositories>
  <repository>
    <id>youtube-dl-wrapper</id>
    <url>https://repo.knighthat.me/repository/maven-public/</url>
  </repository>
</repositories>

<dependencies>
  <dependency>
    <groupId>me.knighthat.youtube-dl</groupId>
    <artifactId>youtube-dl</artifactId>
    <version>0.0.2-SNAPSHOTS</version>
  </dependency>
</dependencies>
```

## Gradle
```groovy
repositories {
    maven {
        url 'https://repo.knighthat.me/repository/maven-public/'
    }
}

dependencies {
    implementation 'me.knighthat.youtube-dl:youtube-dl:0.0.2-SNAPSHOT'
}
```

# License

This project inherits license from [youtube-dl](https://github.com/ytdl-org/youtube-dl/blob/master/LICENSE) which is [**Unlicense License**](https://unlicense.org/)

