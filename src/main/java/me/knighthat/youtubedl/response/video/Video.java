package me.knighthat.youtubedl.response.video;

import me.knighthat.youtubedl.response.format.Format;
import me.knighthat.youtubedl.response.subtitle.Subtitle;
import me.knighthat.youtubedl.response.thumbnail.Thumbnail;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Unmodifiable;

import java.util.Set;


/**
 * This interface represents general information of a video.
 */
public interface Video {

    @NotNull String id();

    @NotNull String title();

    @NotNull @Unmodifiable Set<? extends Thumbnail> thumbnails();

    @NotNull @Unmodifiable Set<? extends Subtitle> subtitles();

    @NotNull @Unmodifiable Set<? extends Format> formats();   
}
