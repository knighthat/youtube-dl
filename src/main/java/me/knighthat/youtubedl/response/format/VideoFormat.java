package me.knighthat.youtubedl.response.format;

import org.jetbrains.annotations.NotNull;

/**
 * VideoFormat
 */
public interface VideoFormat extends Format {

    @NotNull String vCodec();

    @NotNull String resolution();

    float fps();
}