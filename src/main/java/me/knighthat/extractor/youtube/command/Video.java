package me.knighthat.extractor.youtube.command;

import java.math.BigInteger;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
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

import me.knighthat.extractor.youtube.response.Channel;
import me.knighthat.extractor.youtube.response.format.Audio;
import me.knighthat.extractor.youtube.response.format.Mix;
import me.knighthat.extractor.youtube.response.subtitle.DownloadableSubtitle;
import me.knighthat.extractor.youtube.response.thumbnail.Thumbnail;
import me.knighthat.internal.annotation.Second;
import me.knighthat.youtubedl.command.Json;
import me.knighthat.youtubedl.command.flag.Flag;
import me.knighthat.youtubedl.command.flag.GeoConfig;
import me.knighthat.youtubedl.command.flag.Header;
import me.knighthat.youtubedl.command.flag.UserAgent;
import me.knighthat.youtubedl.exception.UnsupportedSubtitleFormatException;
import me.knighthat.youtubedl.logging.Logger;
import me.knighthat.youtubedl.response.OptionalResponse;
import me.knighthat.youtubedl.response.format.Format;
import me.knighthat.youtubedl.response.subtitle.Subtitle;

/**
 * YouTube's video's information extractor.
 */
public class Video extends me.knighthat.youtubedl.command.Video {

    @NotNull
    private static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat( "yyyyMMdd" );
    @NotNull
    private static final Gson GSON = new Gson();

    private static @NotNull Set<DownloadableSubtitle> subtitleSet( @NotNull JsonObject json, boolean isAutomatic ) {
        Set<DownloadableSubtitle> subtitles = new HashSet<>();

        for (String language : json.keySet()) {
            for (JsonElement formatKey : json.getAsJsonArray(language)) {
                JsonObject jsonFormat = formatKey.getAsJsonObject();

                try {   
                    subtitles.add( SubtitleImpl.fromJson(language, isAutomatic, jsonFormat) );
                } catch (UnsupportedSubtitleFormatException e) {
                    Logger.exception("failed to parse subtitle!", e, Level.WARNING);
                }
            }
        }

        return subtitles;
    }

    private static @NotNull Set<Format> formatSet( @NotNull JsonObject json ) {
        Set<Format> formats = new HashSet<>();

        for ( JsonElement element : json.getAsJsonArray( "formats" ) ) {
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
                if ( !vcodecNull )
                    format = new Audio( formatJson );
                else
                    format = new me.knighthat.extractor.youtube.response.format.Video( formatJson );

                formats.add( format );
            } catch ( NullPointerException ignored ) {
                String msg = "failed to parsed json:\n" + GSON.toJson(formatJson);
                Logger.exception(msg, ignored, Level.WARNING);
            } 
        }

        return formats;
    }

    public static @NotNull Builder builder( @NotNull String url ) { return new Builder( url ); }

    private Video(
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

        JsonElement videoJson = Json.builder( url() )
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
        Channel channel = ChannelImpl.fromJson( json );

        /* THUMBNAILS */
        Set<Thumbnail> thumbnails = new HashSet<>();
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
            thumbnails.add( ThumbnailImpl.fromJson( thumbJson ) );
        }

        /* CAPTION */
        Set<DownloadableSubtitle> subtitles = new HashSet<>();
        if (json.has("automatic_captions")) 
            subtitles.addAll( subtitleSet( json.getAsJsonObject( "automatic_captions" ), true) );
        if (json.has("subtitles")) 
            subtitles.addAll(subtitleSet(json.getAsJsonObject( "subtitles" ), false));

        Set<Format> formats = formatSet( json );

        return () -> Optional.of(
            new YoutubeVideo(
                json.get( "id" ).getAsString(), 
                json.get( "title" ).getAsString(), 
                thumbnails, 
                subtitles, 
                formats, 
                json.get("description").getAsString(), 
                uploadDate, 
                json.get( "duration" ).getAsLong(), 
                GSON.fromJson( json.get( "view_count" ), BigInteger.class ), 
                GSON.fromJson( json.get( "like_count" ), BigInteger.class ), 
                channel
            )
        );
    }

    public static class Builder extends me.knighthat.youtubedl.command.Video.Builder {

        private Builder(@NotNull String url) { super(url); }
    
        @Override
        public @NotNull Builder flags( @NotNull Flag... flags ) { return (Builder) super.flags( flags ); }

        @Override
        public @NotNull Builder headers( @NotNull Header... headers ) { return (Builder) super.headers( headers ); }

        @Override
        public @NotNull Builder userAgent( @Nullable UserAgent userAgent ) { return (Builder) super.userAgent( userAgent ); }

        @Override
        public @NotNull Builder geoConfig( @Nullable GeoConfig geoConfig ) { return (Builder) super.geoConfig( getGeoConfig() ); }

        @Override
        public @NotNull Video build() {
            return new Video( getUrl(), getFlags(), getHeaders(), getUserAgent(), getGeoConfig() );
        }
    }

    private static record SubtitleImpl(
        @NotNull String language,
        @NotNull Subtitle.Format format,
        boolean isAutomatic,
        @NotNull String url
    ) implements DownloadableSubtitle {

        static @NotNull SubtitleImpl fromJson( @NotNull String language, boolean isAutomatic, @NotNull JsonObject json ) {
            String ext = json.get( "ext" ).getAsString();
            return new SubtitleImpl(
                language, 
                Subtitle.Format.match( ext ), 
                isAutomatic, 
                json.get("url").getAsString()
            );
        }
    }

    private static record ChannelImpl(
        @NotNull String id,
        @NotNull String handle,
        @NotNull String title
    ) implements Channel {

        static @NotNull ChannelImpl fromJson( @NotNull JsonObject json ) {
            return new ChannelImpl(
                json.get( "channel_id" ).getAsString(), 
                json.get( "uploader_id" ).getAsString(), 
                json.get("channel").getAsString()
            );
        }

        @Override
        public @NotNull String uploaderUrl() { return "https://youtube.com/" + handle; }

        @Override
        public @NotNull String channelUrl() { return "https://youtube.com/channel/" + id; }
    }

    private static record ThumbnailImpl(
        @NotNull String url,
        int width,
        int height
    ) implements Thumbnail {

        static @NotNull ThumbnailImpl fromJson( @NotNull JsonObject json ) {
            return new ThumbnailImpl(
                json.get( "url" ).getAsString(), 
                json.get( "width" ).getAsInt(), 
                json.get( "height" ).getAsInt()
            );
        }
    }

    private static record YoutubeVideo (
        @NotNull String id,
        @NotNull String title,
        @NotNull @Unmodifiable Set<Thumbnail> thumbnails,
        @NotNull @Unmodifiable Set<DownloadableSubtitle> subtitles,
        @NotNull @Unmodifiable Set<Format> formats,
        @NotNull String description,
        @NotNull Date uploadDate,
        @Second long duration,
        @NotNull BigInteger views,
        @NotNull BigInteger likes,
        @NotNull Channel uploader
    ) implements me.knighthat.extractor.youtube.response.Video {
    }
}