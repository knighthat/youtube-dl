package me.knighthat.youtubedl.command;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import me.knighthat.youtubedl.command.flag.Flag;
import me.knighthat.youtubedl.command.flag.GeoConfig;
import me.knighthat.youtubedl.command.flag.Header;
import me.knighthat.youtubedl.command.flag.UserAgent;
import me.knighthat.youtubedl.logging.Logger;
import me.knighthat.youtubedl.response.OptionalResponse;
import me.knighthat.youtubedl.response.channel.Channel;
import me.knighthat.youtubedl.response.format.AudioOnly;
import me.knighthat.youtubedl.response.format.Format;
import me.knighthat.youtubedl.response.format.Mix;
import me.knighthat.youtubedl.response.format.VideoOnly;
import me.knighthat.youtubedl.response.subtitle.Subtitle;
import me.knighthat.youtubedl.response.thumbnail.Thumbnail;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.math.BigInteger;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.logging.Level;

public final class Video extends Command {

    @NotNull
    private static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat( "yyyyMMdd" );

    public static @NotNull Builder builder( @NotNull String url ) { return new Builder( url ); }

    private Video( @NotNull String url, @NotNull Set<Flag> flags, @NotNull Set<Header> headers, @Nullable UserAgent userAgent, @Nullable GeoConfig geoConfig ) {
        super( url, flags, headers, userAgent, geoConfig );
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

        Channel channel = new Channel(
                json.get( "channel_id" ).getAsString(),
                json.get( "uploader_id" ).getAsString()
        );

        Set<Thumbnail> thumbnails = new HashSet<>();
        for (JsonElement element : json.getAsJsonArray( "thumbnails" )) {
            /*
            {
              "height": 94,
              "url": "https://i.ytimg.com/vi/Z42INxQkwCs/hqdefault.jpg",
              "width": 168,
              "resolution": "168x94",
              "id": "0"
            }
            */
            JsonObject thumbJson = element.getAsJsonObject();
            thumbnails.add( new Thumbnail(
                    thumbJson.get( "width" ).getAsInt(),
                    thumbJson.get( "height" ).getAsInt(),
                    thumbJson.get( "url" ).getAsString()
            ) );
        }

        List<Subtitle> subtitles = Subtitles.builder( url() )
                                            .flags( flags )
                                            .headers( headers )
                                            .userAgent( userAgent() )
                                            .geoConfig( geoConfig() )
                                            .execute()
                                            .items();

        JsonArray formatArray = json.getAsJsonArray( "formats" );
        Set<Format> formats = new HashSet<>( formatArray.size() );
        for (JsonElement element : formatArray) {
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
                else if ( !vcodecNull )
                    format = new VideoOnly( formatJson );
                else
                    format = new AudioOnly( formatJson );

                formats.add( format );
            } catch ( NullPointerException ignored ) {
                Logger.warning( "failed to parse format form Json" );
                Logger.warning( Json.GSON.toJson( formatJson ) );
            }
        }

        try {
            me.knighthat.youtubedl.response.video.Video video =
                    new me.knighthat.youtubedl.response.video.Video(
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

            return () -> Optional.of( video );
        } catch ( ParseException e ) {
            String uploadDate = json.get( "upload_date" ).getAsString();
            Logger.exception( "failed to parse date" + uploadDate, e, Level.WARNING );
            
            return Optional::empty;
        }
    }

    public static class Builder extends Command.Builder {
        private Builder( @NotNull String url ) { super( url ); }

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
            super.setGeoConfig( getGeoConfig() );
            return this;
        }

        @Override
        public @NotNull Video build() { return new Video( getUrl(), getFlags(), getHeaders(), getUserAgent(), getGeoConfig() ); }

        @Override
        public @NotNull OptionalResponse<me.knighthat.youtubedl.response.video.Video> execute() { return this.build().execute(); }
    }
}
