package me.knighthat.youtubedl.response.formats;

import org.jetbrains.annotations.NotNull;

/**
 * 
 */
public interface AudioFormat extends Format, MediaBitrate {

   @NotNull String aCodec(); 

   int sampleRate();
}