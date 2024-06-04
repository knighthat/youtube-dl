package me.knighthat.youtubedl.command;

import me.knighthat.youtubedl.command.flag.Flag;
import me.knighthat.youtubedl.command.flag.GeoConfig;
import me.knighthat.youtubedl.command.flag.Header;
import me.knighthat.youtubedl.command.flag.UserAgent;
import me.knighthat.youtubedl.response.ListResponse;
import me.knighthat.youtubedl.response.subtitle.Subtitle;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * Subtitles
 */
public interface Subtitles extends Command {

    @NotNull ListResponse<? extends Subtitle> execute();

    interface Builder extends Command.Builder {

        @NotNull Builder flags( @NotNull Flag... flags );

        @NotNull Builder headers( @NotNull Header... headers );

        @NotNull Builder userAgent( @Nullable UserAgent userAgent );

        @NotNull Builder geoConfig( @Nullable GeoConfig geoConfig );

        @NotNull Subtitles build();

        @NotNull ListResponse<? extends Subtitle> execute();
    }
}