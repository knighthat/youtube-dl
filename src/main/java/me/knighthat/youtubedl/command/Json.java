package me.knighthat.youtubedl.command;

import com.google.gson.JsonElement;
import me.knighthat.youtubedl.command.flag.Flag;
import me.knighthat.youtubedl.command.flag.GeoConfig;
import me.knighthat.youtubedl.command.flag.Header;
import me.knighthat.youtubedl.command.flag.UserAgent;
import me.knighthat.youtubedl.response.SingleResultResponse;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public interface Json extends Command {

    @NotNull SingleResultResponse<JsonElement> execute();

    interface Builder extends Command.Builder {

        @NotNull Builder flags( @NotNull Flag... flags );

        @NotNull Builder headers( @NotNull Header... headers );

        @NotNull Builder userAgent( @Nullable UserAgent userAgent );

        @NotNull Builder geoConfig( @Nullable GeoConfig geoConfig );

        @NotNull Json build();

        @NotNull SingleResultResponse<JsonElement> execute();
    }
}
