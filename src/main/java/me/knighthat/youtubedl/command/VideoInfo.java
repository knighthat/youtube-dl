package me.knighthat.youtubedl.command;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import lombok.SneakyThrows;
import me.knighthat.youtubedl.command.flag.CommandFlag;
import me.knighthat.youtubedl.command.flag.GeoConfig;
import me.knighthat.youtubedl.command.flag.HttpHeader;
import me.knighthat.youtubedl.command.flag.UserAgent;
import me.knighthat.youtubedl.response.OptionalResponse;
import me.knighthat.youtubedl.response.channel.Channel;
import me.knighthat.youtubedl.response.format.AudioOnly;
import me.knighthat.youtubedl.response.format.Format;
import me.knighthat.youtubedl.response.format.Mix;
import me.knighthat.youtubedl.response.format.VideoOnly;
import me.knighthat.youtubedl.response.subtitle.Subtitle;
import me.knighthat.youtubedl.response.thumbnail.Thumbnail;
import me.knighthat.youtubedl.response.video.Video;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.math.BigInteger;
import java.text.SimpleDateFormat;
import java.util.*;

public class VideoInfo extends YtdlCommand {

    @NotNull
    private static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat( "yyyyMMdd" );

    public static @NotNull Builder builder( @NotNull String url ) { return new Builder( url ); }

    VideoInfo( @NotNull String url, @NotNull Set<CommandFlag> flags, @NotNull Set<HttpHeader> headers, @NotNull UserAgent userAgent, @Nullable GeoConfig geoConfig ) {
        super( url, flags, headers, userAgent, geoConfig );
    }

    private @Nullable VideoOnly video( @NotNull JsonObject json, @NotNull String codec ) {
        if (
                !json.has( "format_id" ) ||
                !json.has( "tbr" ) ||
                !json.has( "fps" ) ||
                !json.has( "format_note" ) ||
                !json.has( "ext" ) ||
                !json.has( "filesize" )
        )
            return null;

        return new VideoOnly(
                json.get( "format_id" ).getAsInt(),
                json.get( "ext" ).getAsString(),
                (int) json.get( "tbr" ).getAsFloat(),
                json.get( "filesize" ).getAsInt(),
                json.get( "format_note" ).getAsString(),
                json.get( "fps" ).getAsInt(),
                codec
        );
    }

    private @Nullable AudioOnly audio( @NotNull JsonObject json, @NotNull String codec ) {
        if (
                !json.has( "format_id" ) ||
                !json.has( "tbr" ) ||
                !json.has( "asr" ) ||
                !json.has( "ext" ) ||
                !json.has( "filesize" )
        )
            return null;

        return new AudioOnly(
                json.get( "format_id" ).getAsInt(),
                json.get( "ext" ).getAsString(),
                (int) json.get( "tbr" ).getAsFloat(),
                json.get( "filesize" ).getAsInt(),
                json.get( "asr" ).getAsInt(),
                codec
        );
    }

    private @Nullable Mix mix( @NotNull JsonObject json, @NotNull String vcodec, @NotNull String acodec ) {
        if (
                !json.has( "format_id" ) ||
                !json.has( "tbr" ) ||
                !json.has( "fps" ) ||
                !json.has( "format_note" ) ||
                !json.has( "asr" ) ||
                !json.has( "ext" )
        )
            return null;

        return new Mix(
                json.get( "format_id" ).getAsInt(),
                json.get( "ext" ).getAsString(),
                (int) json.get( "tbr" ).getAsFloat(),
                json.get( "format_note" ).getAsString(),
                json.get( "fps" ).getAsInt(),
                json.get( "asr" ).getAsInt(),
                vcodec,
                acodec
        );
    }

    @SneakyThrows
    @Override
    public @NotNull Result execute() {
        Json.JsonResult jsonResult = new Json( url, new HashSet<>( flags ), new HashSet<>( headers ), userAgent, geoConfig ).execute();
        if ( !(jsonResult.json() instanceof JsonObject json) )
            return new Result( null );

        Channel channel = new Channel(
                json.get( "channel_id" ).getAsString(),
                json.get( "uploader_id" ).getAsString()
        );

        Set<Thumbnail> thumbnails = new HashSet<>();
        for (JsonElement element : json.getAsJsonArray( "thumbnails" )) {
            JsonObject thumbObj = element.getAsJsonObject();
            thumbnails.add( new Thumbnail(
                    thumbObj.get( "width" ).getAsInt(),
                    thumbObj.get( "height" ).getAsInt(),
                    thumbObj.get( "url" ).getAsString()
            ) );
        }

        List<Subtitle> subtitles = new Subtitles( url, new HashSet<>( flags ), new HashSet<>( headers ), userAgent, geoConfig ).execute().items();

        Set<Format> formats = new HashSet<>();
        for (JsonElement element : json.getAsJsonArray( "formats" )) {
            JsonObject formatJson = element.getAsJsonObject();

            String vcodec = formatJson.get( "vcodec" ).getAsString();
            String acodec = formatJson.get( "acodec" ).getAsString();

            Format format;

            if ( !vcodec.equalsIgnoreCase( "none" ) && !acodec.equalsIgnoreCase( "none" ) )
                format = mix( formatJson, vcodec, acodec );
            else if ( !vcodec.equalsIgnoreCase( "none" ) )
                format = video( formatJson, vcodec );
            else
                format = audio( formatJson, acodec );

            if ( format == null )
                System.out.println( Json.GSON.toJson( formatJson ) );

            formats.add( format );
        }

        Video video = new Video(
                json.get( "id" ).getAsString(),
                json.get( "title" ).getAsString(),
                json.get( "description" ).getAsString(),
                DATE_FORMAT.parse( json.get( "upload_date" ).getAsString() ),
                json.get( "duration" ).getAsLong(),
                Json.GSON.fromJson( json.get( "view_count" ), BigInteger.class ),
                Json.GSON.fromJson( json.get( "like_count" ), BigInteger.class ),
                thumbnails,
                new HashSet<>( subtitles ),
                formats,
                channel
        );

        return new Result( video );
    }

    public static class Builder extends YtdlCommand.Builder {

        private Builder( @NotNull String url ) { super( url ); }

        public @NotNull Builder flags( @NotNull CommandFlag... flags ) {
            super.flags().addAll( Arrays.asList( flags ) );
            return this;
        }

        public @NotNull Builder headers( @NotNull HttpHeader... headers ) {
            super.headers().addAll( Arrays.asList( headers ) );
            return this;
        }

        public @NotNull Builder userAgent( @NotNull UserAgent userAgent ) {
            super.userAgent( userAgent );
            return this;
        }

        public @NotNull Builder geoConfig( @NotNull GeoConfig geoConfig ) {
            super.geoConfig( geoConfig );
            return this;
        }

        @Override
        public @NotNull VideoInfo build() { return new VideoInfo( url(), flags(), headers(), userAgent(), geoConfig() ); }

        @Override
        public @NotNull Result execute() { return this.build().execute(); }
    }

    public record Result( @Nullable Video video ) implements OptionalResponse<Video> {

        @Override
        public @NotNull Optional<Video> result() { return Optional.ofNullable( video ); }
    }
}
