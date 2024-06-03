package me.knighthat.extractor.youtube.response;

import java.math.BigInteger;
import java.util.Date;
import java.util.Set;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Unmodifiable;

import me.knighthat.extractor.youtube.YouTube;
import me.knighthat.internal.annotation.Second;

/**
 * Detail information of a YouTube video.
 */
public interface YouTubeVideo extends me.knighthat.youtubedl.response.video.Video {

    @NotNull String description();

    @NotNull Date uploadDate();

    @Second long duration();
    
    @NotNull BigInteger views();

    @NotNull BigInteger likes();
    
    @NotNull YouTubeChannel uploader();

    @NotNull @Unmodifiable Set<YouTube.Thumbnail> thumbnails();

    @NotNull @Unmodifiable Set<YouTube.DownloadableSubtitle> subtitles();
}