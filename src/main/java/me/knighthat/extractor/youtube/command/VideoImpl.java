package me.knighthat.extractor.youtube.command;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import me.knighthat.extractor.youtube.YouTube;
import me.knighthat.extractor.youtube.response.format.Audio;
import me.knighthat.extractor.youtube.response.format.Mix;
import me.knighthat.extractor.youtube.response.thumbnail.Thumbnail;
import me.knighthat.internal.annotation.Second;
import me.knighthat.youtubedl.command.JsonImpl;
import me.knighthat.youtubedl.command.flag.Flag;
import me.knighthat.youtubedl.command.flag.GeoConfig;
import me.knighthat.youtubedl.command.flag.Header;
import me.knighthat.youtubedl.command.flag.UserAgent;
import me.knighthat.youtubedl.exception.UnsupportedSubtitleFormatException;
import me.knighthat.youtubedl.logging.Logger;
import me.knighthat.youtubedl.response.OptionalResponse;
import me.knighthat.youtubedl.response.format.Format;
import me.knighthat.youtubedl.response.subtitle.Subtitle;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.annotations.Unmodifiable;

import java.math.BigInteger;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import java.util.logging.Level;

/**
 * VideoImpl
 */
public class VideoImpl extends me.knighthat.youtubedl.command.VideoImpl implements Video {

    @NotNull
    private static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat( "yyyyMMdd" );
    @NotNull
    private static final Gson             GSON        = new Gson();

    private static @NotNull Set<YouTube.DownloadableSubtitle> subtitleSet( @NotNull JsonObject json, boolean isAutomatic ) {
        Set<YouTube.DownloadableSubtitle> subtitles = new HashSet<>();

        for (String language : json.keySet()) {
            for (JsonElement formatKey : json.getAsJsonArray( language )) {
                JsonObject jsonFormat = formatKey.getAsJsonObject();

                try {
                    subtitles.add( new YouTubeSubtitleImpl( language, isAutomatic, jsonFormat ) );
                } catch ( UnsupportedSubtitleFormatException e ) {
                    Logger.exception( "failed to parse subtitle!", e, Level.WARNING );
                }
            }
        }

        return subtitles;
    }

    private static @NotNull Set<Format> formatSet( @NotNull JsonObject json ) {
        Set<Format> formats = new HashSet<>();

        for (JsonElement element : json.getAsJsonArray( "formats" )) {
            JsonObject formatJson = element.getAsJsonObject();

            boolean vcodecNull = formatJson.get( "vcodec" )
                                           .getAsString()
                                           .equalsIgnoreCase( "none" );
            boolean acodecNull = formatJson.get( "acodec" )
                                           .getAsString()
                                           .equalsIgnoreCase( "none" );

            try {
                Format format;
                if ( !vcodecNull && !acodecNull )
                    format = new Mix( formatJson );
                else if ( vcodecNull )
                    format = new Audio( formatJson );
                else
                    format = new me.knighthat.extractor.youtube.response.format.Video( formatJson );

                formats.add( format );
            } catch ( NullPointerException e ) {
                String msg = "failed to parsed json:\n" + GSON.toJson( formatJson );
                Logger.exception( msg, e, Level.WARNING );
            }
        }

        return formats;
    }

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
    public @NotNull OptionalResponse<YouTube.Video> execute() {
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

        /* UPLOAD DATE */
        Date uploadDate;
        try {
            uploadDate = DATE_FORMAT.parse( json.get( "upload_date" ).getAsString() );
        } catch ( ParseException e ) {
            String message = json.get( "upload_date" ).getAsString();
            Logger.exception( "failed to parse date" + message, e, Level.WARNING );

            return Optional::empty;
        }

        /* CHANNEL */
        YouTube.Channel channel = new YouTubeChannelImpl( json );

        /* THUMBNAILS */
        Set<YouTube.Thumbnail> thumbnails = new HashSet<>();
        for (JsonElement element : json.getAsJsonArray( "thumbnails" )) {
            /*
            {
              "height": 94,
              "url": "https://i.ytimg.com/vi/videoId/hqdefault.jpg",
              "width": 168,
              "resolution": "168x94",
              "id": "0"
            }
            */
            JsonObject thumbJson = element.getAsJsonObject();
            thumbnails.add( new Thumbnail( thumbJson ) );
        }

        /* CAPTION */
        Set<YouTube.DownloadableSubtitle> subtitles = new HashSet<>();
        if ( json.has( "automatic_captions" ) )
            subtitles.addAll( subtitleSet( json.getAsJsonObject( "automatic_captions" ), true ) );
        if ( json.has( "subtitles" ) )
            subtitles.addAll( subtitleSet( json.getAsJsonObject( "subtitles" ), false ) );

        Set<Format> formats = formatSet( json );

        return () -> Optional.of(
            new YouTubeVideoImpl(
                json.get( "id" ).getAsString(),
                json.get( "title" ).getAsString(),
                thumbnails,
                subtitles,
                formats,
                json.get( "description" ).getAsString(),
                uploadDate,
                json.get( "duration" ).getAsLong(),
                GSON.fromJson( json.get( "view_count" ), BigInteger.class ),
                GSON.fromJson( json.get( "like_count" ), BigInteger.class ),
                channel
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
        public @NotNull OptionalResponse<YouTube.Video> execute() { return this.build().execute(); }

        @Override
        public @NotNull Video build() {
            return new VideoImpl( getUrl(), getFlags(), getHeaders(), getUserAgent(), getGeoConfig() );
        }
    }

    private record YouTubeVideoImpl(
        @NotNull String id,
        @NotNull String title,
        @NotNull @Unmodifiable Set<YouTube.Thumbnail> thumbnails,
        @NotNull @Unmodifiable Set<YouTube.DownloadableSubtitle> subtitles,
        @NotNull @Unmodifiable Set<Format> formats,
        @NotNull String description,
        @NotNull Date uploadDate,
        @Second long duration,
        @NotNull BigInteger views,
        @NotNull BigInteger likes,
        @NotNull YouTube.Channel uploader
    ) implements YouTube.Video {
    }

    private record YouTubeChannelImpl(
        @NotNull String id,
        @NotNull String handle,
        @NotNull String title
    ) implements YouTube.Channel {

        private YouTubeChannelImpl( @NotNull JsonObject json ) {
            this(
                json.get( "channel_id" ).getAsString(),
                json.get( "uploader_id" ).getAsString(),
                json.get( "channel" ).getAsString()
            );
        }

        @Override
        public @NotNull String uploaderUrl() { return "https://youtube.com/" + handle; }

        @Override
        public @NotNull String channelUrl() { return "https://youtube.com/channel/" + id; }
    }

    private record YouTubeSubtitleImpl(
        @NotNull String language,
        @NotNull Subtitle.Format format,
        boolean isAutomatic,
        @NotNull String url
    ) implements YouTube.DownloadableSubtitle {

        private YouTubeSubtitleImpl( @NotNull String language, boolean isAutomatic, @NotNull JsonObject json ) {
            this(
                language,
                Subtitle.Format.match( json.get( "ext" ).getAsString() ),
                isAutomatic,
                json.get( "url" ).getAsString()
            );
        }
    }
}