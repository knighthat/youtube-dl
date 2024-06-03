package me.knighthat.extractor.youtube.command;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import me.knighthat.extractor.youtube.YouTube;
import me.knighthat.youtubedl.command.flag.Flag;
import me.knighthat.youtubedl.command.flag.GeoConfig;
import me.knighthat.youtubedl.command.flag.Header;
import me.knighthat.youtubedl.command.flag.UserAgent;
import me.knighthat.youtubedl.response.ListResponse;

public interface Thumbnails extends me.knighthat.youtubedl.command.Thumbnails {

    @Override
    @NotNull ListResponse<YouTube.Thumbnail> execute();

    public static interface Builder extends me.knighthat.youtubedl.command.Thumbnails.Builder {
    
        @Override
        @NotNull Builder flags( @NotNull Flag... flags );

        @Override
        @NotNull Builder headers( @NotNull Header... headers) ;

        @Override
        @NotNull Builder userAgent( @Nullable UserAgent userAgent );

        @Override
        @NotNull Builder geoConfig( @Nullable GeoConfig geoConfig );

        @Override
        @NotNull Thumbnails build();

        @Override
        @NotNull ListResponse<YouTube.Thumbnail> execute();
    }
}