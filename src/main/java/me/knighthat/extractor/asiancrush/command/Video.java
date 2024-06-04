package me.knighthat.extractor.asiancrush.command;

import me.knighthat.extractor.asiancrush.AsianCrush;
import me.knighthat.youtubedl.command.flag.Flag;
import me.knighthat.youtubedl.command.flag.GeoConfig;
import me.knighthat.youtubedl.command.flag.Header;
import me.knighthat.youtubedl.command.flag.UserAgent;
import me.knighthat.youtubedl.response.OptionalResponse;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public interface Video extends me.knighthat.youtubedl.command.Video {

    @NotNull OptionalResponse<AsianCrush.Video> execute();

    interface Builder extends me.knighthat.youtubedl.command.Video.Builder {

        @NotNull Builder flags( @NotNull Flag... flags );

        @NotNull Builder headers( @NotNull Header... headers );

        @NotNull Builder userAgent( @Nullable UserAgent userAgent );

        @NotNull Builder geoConfig( @Nullable GeoConfig geoConfig );

        @NotNull Video build();

        @NotNull OptionalResponse<AsianCrush.Video> execute();
    }
}