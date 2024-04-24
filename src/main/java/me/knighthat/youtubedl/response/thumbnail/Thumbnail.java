package me.knighthat.youtubedl.response.thumbnail;

import org.jetbrains.annotations.NotNull;

public record Thumbnail( int width, int height, @NotNull String url ) {
}
