package me.knighthat.youtubedl.response.format;

import org.jetbrains.annotations.NotNull;

/**
 * A basic representation of a video's format.
 */
public interface Format {

    @NotNull Type type();

    @NotNull String code();

    @NotNull String extension();

    public enum Type {
        VIDEO_ONLY,
        AUDIO_ONLY,
        MIXED
    }
}