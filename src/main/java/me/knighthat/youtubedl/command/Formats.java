package me.knighthat.youtubedl.command;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import me.knighthat.youtubedl.command.flag.Flag;
import me.knighthat.youtubedl.command.flag.GeoConfig;
import me.knighthat.youtubedl.command.flag.Header;
import me.knighthat.youtubedl.command.flag.UserAgent;
import me.knighthat.youtubedl.response.ListResponse;
import me.knighthat.youtubedl.response.format.Format;

public interface Formats extends Command {

    @Override
    @NotNull ListResponse<Format> execute();

    public static interface Builder extends Command.Builder {

        @Override
        @NotNull Builder flags( @NotNull Flag... flags );

        @Override
        @NotNull Builder headers( @NotNull Header... headers) ;

        @Override
        @NotNull Builder userAgent( @Nullable UserAgent userAgent );

        @Override
        @NotNull Builder geoConfig( @Nullable GeoConfig geoConfig );
    
        @Override
        @NotNull Formats build();

        @Override
        @NotNull ListResponse<Format> execute();
    }
}