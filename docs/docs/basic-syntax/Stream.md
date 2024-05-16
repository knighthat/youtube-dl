---
sidebar_position: 3
slug: stream
---

# Stream

Make a request to the provided **URL** and let user access the datastream returned from the server in real-time.

> As of version 0.0.2, only YouTube links are tested. Other websites are in development.

```javascript
YoutubeDL.stream(URL);
```

Replace `URL` with a supported media website.

Note that the code snippet returns `Stream.Builder` instead of `RealtimeResponse`.
That's because the code has not been executed yet.

`Stream.Builder` allows you to add additional flags such as **GeoConfig**, **Network**, **Download**, **UserAgent**, and **Header**
to command before deploying it. [**Explore flags**](/docs/category/flags).

## `.build()`

At any given time after the initialization of `Stream.Builder`, you can invoke `build()` to finalize the command
and marked it as _ready to use_. This locks the command from further modification.

> Finalized `Stream` class only have two options. To `execute()` or to get provided flags.

## `.execute()`

This method combines `build()` and `execute()` from **Stream**.

So, instead of:

```javascript
YoutubeDL.stream(URL).build().execute();
```

You can do:

```javascript
YoutubeDL.stream(URL).execute();
```

:::tip
`execute()` technically does not make the request.\
Because this is real-time data being stream over the internet,
unless there's a handler to process the data, no data
will go through and will be put into idle state.
:::

## `.stream(int, BiConsumer<byte[], Integer>)`

This method allows you to access the stream of data in real-time. 

First argument is the size of the buffer (in bytes).\
Second argument is the hanlder of each buffer, first variable is the buffer
in byte array with the size of **_first argument_**, and the second variable
is how many bytes in first variable is actual data.


:::tip
`Stream#execute()` or `Stream.Builder#execute()` does not 
:::

And you'll get an instsance of `SingleResultReponse` as the result of this method.
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