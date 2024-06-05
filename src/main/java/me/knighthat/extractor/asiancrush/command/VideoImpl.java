package me.knighthat.extractor.asiancrush.command;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import me.knighthat.extractor.asiancrush.AsianCrush;
import me.knighthat.extractor.youtube.response.format.Mix;
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
        Optional<AsianCrush.Video> result = Optional.empty();

        try {
            JsonObject json = super.getJson();

            /* FORMATS */
            Set<Format> formats = new HashSet<>();
            for (JsonElement element : json.getAsJsonArray( "formats" )) {
                JsonObject formatJson = element.getAsJsonObject();

                formats.add( new Mix( formatJson ) );
            }

            result = Optional.of(
                new Movie(
                    json.get( "id" ).getAsString(),
                    json.get( "title" ).getAsString(),
                    formats
                )
            );

        } catch ( IllegalStateException e ) {
            Logger.exception( "couldn't get video information from: " + url(), e, Level.SEVERE );
        } catch ( NullPointerException e ) {
            Logger.exception( "failed to parse json!", e, Level.WARNING );
        }

        Optional<AsianCrush.Video> finalResult = result;
        return () -> finalResult;
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