package me.knighthat.youtubedl.response;

import org.jetbrains.annotations.NotNull;

import java.util.List;

@FunctionalInterface
public interface ListResponse<T> extends Response {

    @NotNull List<T> items();
}
