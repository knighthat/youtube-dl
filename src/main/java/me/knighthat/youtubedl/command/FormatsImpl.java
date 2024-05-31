package me.knighthat.youtubedl.command;

import java.util.Set;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import me.knighthat.youtubedl.command.flag.Flag;
import me.knighthat.youtubedl.command.flag.GeoConfig;
import me.knighthat.youtubedl.command.flag.Header;
import me.knighthat.youtubedl.command.flag.UserAgent;
import me.knighthat.youtubedl.response.ListResponse;
import me.knighthat.youtubedl.response.format.Format;

public abstract class FormatsImpl extends CommandImpl implements Formats {

    protected FormatsImpl(
        @NotNull String url, 
        @NotNull Set<Flag> flags, 
        @NotNull Set<Header> headers,
        @Nullable UserAgent userAgent, 
        @Nullable GeoConfig geoConfig
    ) {
        super(url, flags, headers, userAgent, geoConfig);
        flags.add( Flag.noValue( "--list-formats" ) );
    }

    public static abstract class Builder extends CommandImpl.Builder implements Formats.Builder {

        protected Builder(String url) { super(url); }

        @Override
        public @NotNull Builder flags( @NotNull Flag... flags ) { return (Builder) super.flags( flags ); }

        @Override
        public @NotNull Builder headers( @NotNull Header... headers) { return (Builder) super.headers( headers ); }

        @Override
        public @NotNull Builder userAgent( @Nullable UserAgent userAgent ) { return (Builder) super.userAgent( userAgent ); }

        @Override
        public @NotNull Builder geoConfig( @Nullable GeoConfig geoConfig ) { return (Builder) super.geoConfig( geoConfig ); }

        @Override
        public @NotNull ListResponse<Format> execute() { return this.build().execute(); }
    }
}