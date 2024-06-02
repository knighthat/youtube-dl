package me.knighthat.extractor.asiancrush.command;

import java.util.Collections;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import java.util.logging.Level;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.annotations.Unmodifiable;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import me.knighthat.extractor.youtube.response.format.Mix;
import me.knighthat.youtubedl.command.JsonImpl;
import me.knighthat.youtubedl.command.VideoImpl;
import me.knighthat.youtubedl.command.flag.Flag;
import me.knighthat.youtubedl.command.flag.GeoConfig;
import me.knighthat.youtubedl.command.flag.Header;
import me.knighthat.youtubedl.command.flag.UserAgent;
import me.knighthat.youtubedl.logging.Logger;
import me.knighthat.youtubedl.response.OptionalResponse;
import me.knighthat.youtubedl.response.format.Format;
import me.knighthat.youtubedl.response.subtitle.Subtitle;
import me.knighthat.youtubedl.response.thumbnail.Thumbnail;

/**
 * Video's information extractor for AsianCrush
 */
public class Video extends VideoImpl {

    @NotNull
    private static final Gson GSON = new Gson();
    
    public static @NotNull Builder builder( @NotNull String url ) { return new Builder( url ); }

    protected Video(
        @NotNull String url, 
        @NotNull Set<Flag> flags, 
        @NotNull Set<Header> headers,
        @Nullable UserAgent userAgent, 
        @Nullable GeoConfig geoConfig
    ) {
        super(url, flags, headers, userAgent, geoConfig);
    }

    @Override
    public @NotNull OptionalResponse<me.knighthat.youtubedl.response.video.Video> execute() {
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
        for ( JsonElement element : json.getAsJsonArray( "formats" ) ) {
            JsonObject formatJson = element.getAsJsonObject();
            
            try {
                formats.add( new Mix( formatJson ) );
            } catch ( NullPointerException ignored ) {
                String msg = "failed to parsed json:\n" + GSON.toJson(formatJson);
                Logger.exception(msg, ignored, Level.WARNING);
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

    public static class Builder extends VideoImpl.Builder {

        protected Builder( @NotNull String url ) { super( url ); }

        @Override
        public @NotNull Video build() {
            return new Video( getUrl(), getFlags(), getHeaders(), getUserAgent(), getGeoConfig() );
        }
    }

    private static record Movie(
        @NotNull String id,
        @NotNull String title,
        @NotNull @Unmodifiable Set<Format> formats
    ) implements me.knighthat.youtubedl.response.video.Video {

        @Override
        public @NotNull @Unmodifiable Set<Thumbnail> thumbnails() { return Collections.emptySet(); }

        @Override
        public @NotNull @Unmodifiable Set<Subtitle> subtitles() { return Collections.emptySet(); }
    }
}