package me.knighthat.extractor.youtube.command;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import me.knighthat.extractor.youtube.YouTube;
import me.knighthat.youtubedl.command.flag.Flag;
import me.knighthat.youtubedl.command.flag.GeoConfig;
import me.knighthat.youtubedl.command.flag.Header;
import me.knighthat.youtubedl.command.flag.UserAgent;
import me.knighthat.youtubedl.response.OptionalResponse;

/**
 * Video
 */
public interface Video extends me.knighthat.youtubedl.command.Video {

    @NotNull OptionalResponse<YouTube.Video> execute();

    public static interface Builder extends me.knighthat.youtubedl.command.Video.Builder {

        @NotNull Builder flags( @NotNull Flag... flags );

        @NotNull Builder headers( @NotNull Header... headers) ;

        @NotNull Builder userAgent( @Nullable UserAgent userAgent );

        @NotNull Builder geoConfig( @Nullable GeoConfig geoConfig );

        @NotNull Video build();

        @NotNull OptionalResponse<YouTube.Video> execute();
    }
}