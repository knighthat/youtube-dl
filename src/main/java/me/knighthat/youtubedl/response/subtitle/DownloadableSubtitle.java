package me.knighthat.youtubedl.response.subtitle;

import org.jetbrains.annotations.NotNull;

/**
 * This subtitle class provides a URL to download the subtitle
 */
public interface DownloadableSubtitle extends Subtitle {

    @NotNull String url();
}