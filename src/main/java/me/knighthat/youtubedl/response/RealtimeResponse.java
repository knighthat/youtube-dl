package me.knighthat.youtubedl.response;

import org.jetbrains.annotations.NotNull;

import java.util.function.BiConsumer;

@FunctionalInterface
public interface RealtimeResponse extends Response {

    void stream( int bufferSize, @NotNull BiConsumer<byte[], Integer> stream );
}
