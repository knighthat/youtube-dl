---
sidebar_position: 6
slug: video
---

# Video

Make a request to the provided **URL** and parse return values to an instance of **_Video_**.

> As of version 0.0.2, only YouTube links are tested. Other websites are in development.

```javascript
YoutubeDL.video(URL);
```

Replace `URL` with a supported media website.

Note that the code snippet returns `Video.Builder` instead of `SingleResultResponse<Video>`.
That's because the code has not been executed yet.

`Video.Builder` allows you to add additional flags such as **GeoConfig**, **Network**, **Download**, **UserAgent**, and **Header**
to command before deploying it. [**Explore flags**](/docs/category/flags).

## `.build()`

At any given time after the initialization of `Video.Builder`, you can invoke `build()` to finalize the command
and marked it as _ready to use_. This locks the command from further modification.

> Finalized `Video` class only have two options. To `execute()` or to get provided flags.

## `.execute()`

This method combines `build()` and `execute()` from **Video**.

So, instead of:

```javascript
YoutubeDL.video(URL).build().execute();
```

You can do:

```javascript
YoutubeDL.video(URL).execute();
```

And you'll get an instance of `SingleResultReponse` as the result of this method.
[Learn more](/docs/Responses.md#singleresultresponse)

## `.flags( Flag... )`
> This method takes arguments as an **_array_** or **_varargs_**

[Read more.](/docs/flags/Flag.md)

## `.headers( Header... )`
> This method takes arguments as an **_array_** or **_varargs_**

[Read more.](/docs/flags/Header.md)

## `.userAgent( UserAgent )`
> This method can only take **ONE** argument. Any subsequent call overrides the preceding one.

[Read more.](/docs/flags/UserAgent.md)

:::danger
`UserAgent` is a subclass of `Flag`. Meaning it can be added by using `.flags()` method.

This is not recommended because `.flags()` isn't designed to override similar flags.

`youtube-dl` will most likely to ignore multiple `UserAgent` being added to final command but
doesn't mean it's safe to do so 100%.
:::

## `.geoConfig( GeoConfig )`
> This method can only take **ONE** argument. Any subsequent call overrides the preceding one.

[Read more.](/docs/flags/GeoConfig.md)

:::danger
`GeoConfig` is a subclass of `Flag`. Meaning it can be added by using `.flags()` method.

This is not recommended because `.flags()` isn't designed to override similar flags.

`youtube-dl` will most likely to throw error when there are two
or more geographical flags are added with different values.
:::