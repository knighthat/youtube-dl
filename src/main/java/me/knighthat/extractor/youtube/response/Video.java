package me.knighthat.extractor.youtube.response;

import java.math.BigInteger;
import java.util.Date;

import org.jetbrains.annotations.NotNull;

import me.knighthat.internal.annotation.Second;

/**
 * Details information of a YouTube video.
 */
public interface Video extends me.knighthat.youtubedl.response.video.Video {

    @NotNull String description();

    @NotNull Date uploadDate();

    @Second long duration();
    
    @NotNull BigInteger views();

    @NotNull BigInteger likes();
    
    @NotNull Channel uploader();
}