package me.knighthat.youtubedl.command;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import me.knighthat.youtubedl.command.flag.Flag;
import me.knighthat.youtubedl.command.flag.GeoConfig;
import me.knighthat.youtubedl.command.flag.Header;
import me.knighthat.youtubedl.command.flag.UserAgent;
import me.knighthat.youtubedl.response.ListResponse;
import me.knighthat.youtubedl.response.subtitle.Subtitle;

/**
 * Subtitles
 */
public interface Subtitles extends Command {

    @Override
    @NotNull ListResponse<Subtitle> execute();

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
        @NotNull Subtitles build();

        @Override
        @NotNull ListResponse<Subtitle> execute();
    }
}