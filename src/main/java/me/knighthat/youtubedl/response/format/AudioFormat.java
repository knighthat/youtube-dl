package me.knighthat.youtubedl.response.format;

import org.jetbrains.annotations.NotNull;

/**
 * 
 */
public interface AudioFormat extends Format, MediaBitrate {

   @NotNull String aCodec(); 
}