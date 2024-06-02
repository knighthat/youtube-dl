package me.knighthat.extractor.youtube.command;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.logging.Level;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import me.knighthat.extractor.youtube.response.thumbnail.Thumbnail;
import me.knighthat.youtubedl.command.flag.Flag;
import me.knighthat.youtubedl.command.flag.GeoConfig;
import me.knighthat.youtubedl.command.flag.Header;
import me.knighthat.youtubedl.command.flag.UserAgent;
import me.knighthat.youtubedl.command.ThumbnailsImpl;
import me.knighthat.youtubedl.exception.InsufficientElementsException;
import me.knighthat.youtubedl.logging.Logger;
import me.knighthat.youtubedl.response.ListResponse;

public class Thumbnails extends ThumbnailsImpl {

    private static int dimParser( @NotNull String dimStr ) {
        int result = 0;

        try {
            result = Integer.parseInt( dimStr );
        } catch ( NumberFormatException e ) {
            Logger.exception("failed to parse dimension: " + dimStr , e, Level.WARNING);
            Logger.warning("use 0 as default value!");
        }

        return result;
    }

    public static @NotNull Builder builder( @NotNull String url ) { return new Builder( url ); }

    protected Thumbnails(
        @NotNull String url, 
        @NotNull Set<Flag> flags, 
        @NotNull Set<Header> headers,
        @Nullable UserAgent userAgent, 
        @Nullable GeoConfig geoConfig
    ) {
        super(url, flags, headers, userAgent, geoConfig);
    }

    @Override
    public @NotNull ListResponse<me.knighthat.youtubedl.response.thumbnail.Thumbnail> execute() {
        List<me.knighthat.youtubedl.response.thumbnail.Thumbnail> thumbnails = new ArrayList<>();

        /*
        0   168    94     https://i.ytimg.com/vi/videoId/hqdefault.jpg
        1   196    110    https://i.ytimg.com/vi/videoId/hqdefault.jpg
        2   246    138    https://i.ytimg.com/vi/videoId/hqdefault.jpg
        3   336    188    https://i.ytimg.com/vi/videoId/hqdefault.jpg
        4   1920   1080   https://i.ytimg.com/vi/videoId/maxresdefault.jpg
        */
        for (String output : super.outputs()) {
            if ( !Character.isDigit( output.charAt( 0 ) ) )
                continue;

            // [0, 168, 94, https://i.ytimg.com/vi/videoId/hqdefault.jpg]
            String[] parts = output.trim().split( "\\s+" );
            if ( parts.length < 4 )
                throw new InsufficientElementsException( Arrays.toString( parts ), parts.length, 4 );

            thumbnails.add(
                new Thumbnail() {
                    @Override
                    public @NotNull String url() { return parts[3]; }

                    @Override
                    public int width() { return dimParser( parts[1] ); }

                    @Override
                    public int height() { return dimParser( parts[2] ); }
                }
            );
        }

        return () ->  thumbnails;
    }

    public static class Builder extends ThumbnailsImpl.Builder {

        protected Builder( @NotNull String url ) { super( url ); }

        @Override
        public @NotNull Thumbnails build() {
            return new Thumbnails( getUrl(), getFlags(), getHeaders(), getUserAgent(), getGeoConfig() );
        }
    }
}