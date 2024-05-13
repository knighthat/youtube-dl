---
sidebar_position: 6
slug: responses
---

# Responses

## Response

Every return instance is an subclass of this interface.

## ListResponse

When the query is expected to return data in a list configuration.
This instance if return in place of [Response](#response).

`ListResponse` has one method, which is `items()`.\
`items()` returns the actual `List` that contains classes represent
the data returned from the server.

```javascript
ListResponse#items();
```

## SingleResultResponse

When query expects a non list return value, this class will represent that data.

`.result()` can be use to retrieve the actual class that represents this data

:::tip
`SingleResultReponse`'s result cannot be null.

For **nullable** object, use [OptionalResponse](#optionalresponse)
:::

## OptionalResponse

When query expects a non list return value, this class will represent that data.
But the data can be null.

`.result()` can be use to retrieve the `Optional` object that 
holds class that represents this data or **_null_**.

## RealtimeResponse

Unlike other reponses, which is processing data after the datastream has ended.
`RealtimeResponse` allows you to have access to the data as it is downloaded
by chunks (size is provided by you). 