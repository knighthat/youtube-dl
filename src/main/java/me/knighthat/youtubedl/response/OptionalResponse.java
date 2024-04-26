package me.knighthat.youtubedl.response;

import org.jetbrains.annotations.NotNull;

import java.util.Optional;

@FunctionalInterface
public interface OptionalResponse<T> extends Response {

    @NotNull Optional<T> result();
}
