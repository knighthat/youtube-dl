package me.knighthat.extractor.asiancrush.command;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import me.knighthat.extractor.asiancrush.AsianCrush;
import me.knighthat.youtubedl.command.flag.Flag;
import me.knighthat.youtubedl.command.flag.GeoConfig;
import me.knighthat.youtubedl.command.flag.Header;
import me.knighthat.youtubedl.command.flag.UserAgent;
import me.knighthat.youtubedl.response.ListResponse;

/**
 * Formats extractor for AsianCrush website.
 */
public interface Formats extends me.knighthat.youtubedl.command.Formats {

    @NotNull ListResponse<AsianCrush.Movie> execute();

    public static interface Builder extends me.knighthat.youtubedl.command.Formats.Builder {
    
        @NotNull Builder flags( @NotNull Flag... flags );

        @NotNull Builder headers( @NotNull Header... headers );

        @NotNull Builder userAgent(@Nullable UserAgent userAgent);

        @NotNull Builder geoConfig(@Nullable GeoConfig geoConfig);
    
        @NotNull Formats build();

        @NotNull ListResponse<AsianCrush.Movie> execute();
    }
}