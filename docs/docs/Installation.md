---
sidebar_position: 2
slug: installation
---

# Installation

**YoutubeDL Java** is available at my repository and is retrievable if you're using one of these build tools

## Maven

```xml title="pom.xml"
<repositories>
  <repository>
    <id>youtube-dl-wrapper</id>
    <url>https://repo.knighthat.me/repository/maven-public/</url>
  </repository>
</repositories>
```

```xml
<dependencies>
  <dependency>
    <groupId>me.knighthat.youtube-dl</groupId>
    <artifactId>youtube-dl</artifactId>
    <version>0.0.2-SNAPSHOTS</version>
  </dependency>
</dependencies>
```

## Gradle

```groovy title="build.gradle"
repositories {
    maven {
        url 'https://repo.knighthat.me/repository/maven-public/'
    }
}

dependencies {
    implementation 'me.knighthat.youtube-dl:youtube-dl:0.0.2-SNAPSHOT'
}
```