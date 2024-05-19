package me.knighthat.youtubedl.response.video;

import me.knighthat.extractor.youtube.response.Channel;
import me.knighthat.youtubedl.response.format.Format;
import me.knighthat.youtubedl.response.subtitle.Subtitle;
import me.knighthat.youtubedl.response.thumbnail.Thumbnail;
import org.jetbrains.annotations.NotNull;

import java.math.BigInteger;
import java.util.Date;
import java.util.Set;

public record Video( @NotNull String id,
                     @NotNull String title,
                     @NotNull String description,
                     @NotNull Date uploadDate,
                     long duration,
                     @NotNull BigInteger views,
                     @NotNull BigInteger likes,
                     @NotNull Set<Thumbnail> thumbnails,
                     @NotNull Set<Subtitle> subtitles,
                     @NotNull Set<Format> formats,
                     @NotNull Channel uploader
) {
}
