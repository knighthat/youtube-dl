package me.knighthat.youtubedl.response.video;

import me.knighthat.youtubedl.response.format.Format;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Unmodifiable;

import java.util.Set;


/**
 * This interface represents general information of a video.
 */
public interface Video {

    @NotNull String id();

    @NotNull String title();

    @NotNull @Unmodifiable Set<Format> formats();   
}
