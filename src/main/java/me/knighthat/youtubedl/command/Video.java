package me.knighthat.youtubedl.command;

import me.knighthat.youtubedl.command.flag.Flag;
import me.knighthat.youtubedl.command.flag.GeoConfig;
import me.knighthat.youtubedl.command.flag.Header;
import me.knighthat.youtubedl.command.flag.UserAgent;
import me.knighthat.youtubedl.response.OptionalResponse;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Set;

public abstract class Video extends Command {

    protected Video( @NotNull String url, @NotNull Set<Flag> flags, @NotNull Set<Header> headers, @Nullable UserAgent userAgent, @Nullable GeoConfig geoConfig ) {
        super( url, flags, headers, userAgent, geoConfig );
    }

    @Override
    public abstract @NotNull OptionalResponse<me.knighthat.youtubedl.response.video.Video> execute();

    public abstract static class Builder extends Command.Builder {
        
        protected Builder( @NotNull String url ) { super( url ); }

        @Override
        public @NotNull Builder flags( @NotNull Flag... flags ) { return (Builder) super.flags( flags ); }

        @Override
        public @NotNull Builder headers( @NotNull Header... headers ) { return (Builder) super.headers( headers ); }

        @Override
        public @NotNull Builder userAgent( @Nullable UserAgent userAgent ) { return (Builder) super.userAgent( userAgent ); }

        @Override
        public @NotNull Builder geoConfig( @Nullable GeoConfig geoConfig ) { return (Builder) super.geoConfig( geoConfig ); }

        @Override
        public abstract @NotNull Video build();

        @Override
        public @NotNull OptionalResponse<me.knighthat.youtubedl.response.video.Video> execute() { return this.build().execute(); }
    }
}
