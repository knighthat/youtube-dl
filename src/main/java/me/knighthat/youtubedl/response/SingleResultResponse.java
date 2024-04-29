package me.knighthat.youtubedl.response;

import org.jetbrains.annotations.NotNull;

@FunctionalInterface
public interface SingleResultResponse<T> extends Response {

    @NotNull T result();
}
