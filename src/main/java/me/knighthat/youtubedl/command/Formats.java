package me.knighthat.youtubedl.command;

import me.knighthat.youtubedl.command.flag.Flag;
import me.knighthat.youtubedl.command.flag.GeoConfig;
import me.knighthat.youtubedl.command.flag.Header;
import me.knighthat.youtubedl.command.flag.UserAgent;
import me.knighthat.youtubedl.response.ListResponse;
import me.knighthat.youtubedl.response.formats.Format;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Set;

public abstract class Formats extends Command {

    protected Formats( @NotNull String url, @NotNull Set<Flag> flags, @NotNull Set<Header> headers, @NotNull UserAgent userAgent, @NotNull GeoConfig geoConfig ) {
        super( url, flags, headers, userAgent, geoConfig );
        flags().add( Flag.noValue( "--list-formats" ) );
    }

    @Override
    public abstract @NotNull ListResponse<? extends Format> execute();

    public static abstract class Builder extends Command.Builder {

        protected Builder( @NotNull String url ) { super( url ); }

        @Override
        public @NotNull Builder flags( @NotNull Flag... flags ) {
            super.addFlags( flags );
            return this;
        }

        @Override
        public @NotNull Builder headers( @NotNull Header... headers ) {
            super.addHeaders( headers );
            return this;
        }

        @Override
        public @NotNull Builder userAgent( @Nullable UserAgent userAgent ) {
            super.setUserAgent( userAgent );
            return this;
        }

        @Override
        public @NotNull Builder geoConfig( @Nullable GeoConfig geoConfig ) {
            super.setGeoConfig( geoConfig );
            return this;
        }

        @Override
        public abstract @NotNull Formats build();

        @Override
        public @NotNull ListResponse<? extends Format> execute() { return this.build().execute(); }
    }
}
