package me.knighthat.extractor.youtube.command;

import me.knighthat.extractor.youtube.YouTube;
import me.knighthat.youtubedl.command.flag.Flag;
import me.knighthat.youtubedl.command.flag.GeoConfig;
import me.knighthat.youtubedl.command.flag.Header;
import me.knighthat.youtubedl.command.flag.UserAgent;
import me.knighthat.youtubedl.response.ListResponse;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public interface Thumbnails extends me.knighthat.youtubedl.command.Thumbnails {

    @NotNull ListResponse<YouTube.Thumbnail> execute();

    interface Builder extends me.knighthat.youtubedl.command.Thumbnails.Builder {

        @NotNull Builder flags( @NotNull Flag... flags );

        @NotNull Builder headers( @NotNull Header... headers );

        @NotNull Builder userAgent( @Nullable UserAgent userAgent );

        @NotNull Builder geoConfig( @Nullable GeoConfig geoConfig );

        @NotNull Thumbnails build();

        @NotNull ListResponse<YouTube.Thumbnail> execute();
    }
}