package me.knighthat.extractor.asiancrush.command;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import me.knighthat.extractor.asiancrush.AsianCrush;
import me.knighthat.extractor.youtube.response.format.Mix;
import me.knighthat.youtubedl.command.JsonImpl;
import me.knighthat.youtubedl.command.flag.Flag;
import me.knighthat.youtubedl.command.flag.GeoConfig;
import me.knighthat.youtubedl.command.flag.Header;
import me.knighthat.youtubedl.command.flag.UserAgent;
import me.knighthat.youtubedl.logging.Logger;
import me.knighthat.youtubedl.response.OptionalResponse;
import me.knighthat.youtubedl.response.format.Format;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.annotations.Unmodifiable;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import java.util.logging.Level;

public class VideoImpl extends me.knighthat.youtubedl.command.VideoImpl implements Video {

    @NotNull
    private static final Gson GSON = new Gson();

    public static @NotNull Builder builder( @NotNull String url ) { return new Builder( url ); }

    private VideoImpl(
        @NotNull String url,
        @NotNull Set<Flag> flags,
        @NotNull Set<Header> headers,
        @Nullable UserAgent userAgent,
        @Nullable GeoConfig geoConfig
    ) {
        super( url, flags, headers, userAgent, geoConfig );
    }

    @Override
    public @NotNull OptionalResponse<AsianCrush.Video> execute() {
        Flag[] flags = this.flags().toArray( Flag[]::new );
        Header[] headers = this.headers().toArray( Header[]::new );

        JsonElement videoJson = JsonImpl.builder( url() )
                                        .flags( flags )
                                        .headers( headers )
                                        .userAgent( userAgent() )
                                        .geoConfig( geoConfig() )
                                        .execute()
                                        .result();
        if ( !(videoJson instanceof JsonObject json) )
            return Optional::empty;

        Set<Format> formats = new HashSet<>();
        for (JsonElement element : json.getAsJsonArray( "formats" )) {
            JsonObject formatJson = element.getAsJsonObject();

            try {
                formats.add( new Mix( formatJson ) );
            } catch ( NullPointerException e ) {
                String msg = "failed to parsed json:\n" + GSON.toJson( formatJson );
                Logger.exception( msg, e, Level.WARNING );
            }
        }

        return () -> Optional.of(
            new Movie(
                json.get( "id" ).getAsString(),
                json.get( "title" ).getAsString(),
                formats
            )
        );
    }

    public static class Builder extends me.knighthat.youtubedl.command.VideoImpl.Builder implements Video.Builder {

        private Builder( @NotNull String url ) { super( url ); }

        @Override
        public @NotNull Builder flags( @NotNull Flag... flags ) {
            super.flags( flags );
            return this;
        }

        @Override
        public @NotNull Builder headers( @NotNull Header... headers ) {
            super.headers( headers );
            return this;
        }

        @Override
        public @NotNull Builder userAgent( @Nullable UserAgent userAgent ) {
            super.userAgent( userAgent );
            return this;
        }

        @Override
        public @NotNull Builder geoConfig( @Nullable GeoConfig geoConfig ) {
            super.geoConfig( geoConfig );
            return this;
        }

        @Override
        public @NotNull OptionalResponse<AsianCrush.Video> execute() { return this.build().execute(); }

        @Override
        public @NotNull Video build() {
            return new VideoImpl( getUrl(), getFlags(), getHeaders(), getUserAgent(), getGeoConfig() );
        }
    }

    private record Movie(
        @NotNull String id,
        @NotNull String title,
        @NotNull @Unmodifiable Set<Format> formats
    ) implements AsianCrush.Video { }
}