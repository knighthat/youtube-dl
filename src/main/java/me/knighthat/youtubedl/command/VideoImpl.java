package me.knighthat.youtubedl.command;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import me.knighthat.youtubedl.command.flag.Flag;
import me.knighthat.youtubedl.command.flag.GeoConfig;
import me.knighthat.youtubedl.command.flag.Header;
import me.knighthat.youtubedl.command.flag.UserAgent;
import me.knighthat.youtubedl.response.OptionalResponse;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Set;

public abstract class VideoImpl extends CommandImpl implements Video {

    protected VideoImpl(
        @NotNull String url,
        @NotNull Set<Flag> flags,
        @NotNull Set<Header> headers,
        @Nullable UserAgent userAgent,
        @Nullable GeoConfig geoConfig
    ) {
        super( url, flags, headers, userAgent, geoConfig );
    }

    protected @NotNull JsonObject getJson() {
        JsonElement json = JsonImpl.builder( this.url() )
                                   .flags( this.flags().toArray( Flag[]::new ) )
                                   .headers( this.headers().toArray( Header[]::new ) )
                                   .userAgent( this.userAgent() )
                                   .geoConfig( this.geoConfig() )
                                   .execute()
                                   .result();

        if ( json.isJsonObject() )
            return json.getAsJsonObject();
        else
            throw new IllegalStateException( "not a json object!" );
    }

    public static abstract class Builder extends CommandImpl.Builder implements Video.Builder {

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
        public @NotNull OptionalResponse<? extends me.knighthat.youtubedl.response.video.Video> execute() {
            return this.build().execute();
        }
    }
}