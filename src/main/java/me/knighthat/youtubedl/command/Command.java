package me.knighthat.youtubedl.command;

import java.util.Set;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.annotations.Unmodifiable;

import me.knighthat.youtubedl.command.flag.Flag;
import me.knighthat.youtubedl.command.flag.GeoConfig;
import me.knighthat.youtubedl.command.flag.Header;
import me.knighthat.youtubedl.command.flag.UserAgent;
import me.knighthat.youtubedl.response.Response;

public interface Command {

    @NotNull String url();

    @NotNull @Unmodifiable Set<Flag> flags();

    @NotNull @Unmodifiable Set<Header> headers();

    @Nullable UserAgent userAgent();

    @Nullable GeoConfig geoConfig();

    @NotNull Response execute();

    String @NotNull [] command();

    public interface Builder {
    
        @NotNull Builder flags( @NotNull Flag... flags );
       
        @NotNull Builder headers( @NotNull Header... headers );

        @NotNull Builder userAgent( @Nullable UserAgent userAgent );

        @NotNull Builder geoConfig( @Nullable GeoConfig geoConfig );

        @NotNull Command build();

        @NotNull Response execute();
    }
}