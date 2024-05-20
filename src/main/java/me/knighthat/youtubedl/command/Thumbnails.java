package me.knighthat.youtubedl.command;

import me.knighthat.youtubedl.command.flag.Flag;
import me.knighthat.youtubedl.command.flag.GeoConfig;
import me.knighthat.youtubedl.command.flag.Header;
import me.knighthat.youtubedl.command.flag.UserAgent;
import me.knighthat.youtubedl.exception.InsufficientElementsException;
import me.knighthat.youtubedl.response.ListResponse;
import me.knighthat.youtubedl.response.thumbnail.Thumbnail;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;

public final class Thumbnails extends Command {

    public static @NotNull Builder builder( @NotNull String url ) { return new Builder( url ); }

    private Thumbnails( @NotNull String url, @NotNull Set<Flag> flags, @NotNull Set<Header> headers, @Nullable UserAgent userAgent, @Nullable GeoConfig geoConfig ) {
        super( url, flags, headers, userAgent, geoConfig );
        flags().add( Flag.noValue( "--list-thumbnails" ) );
    }

    @Override
    public @NotNull ListResponse<Thumbnail> execute() {
        List<Thumbnail> results = new ArrayList<>();

        /*
        0   168    94     https://i.ytimg.com/vi/JLQTiFwBVyI/hqdefault.jpg
        1   196    110    https://i.ytimg.com/vi/JLQTiFwBVyI/hqdefault.jpg
        2   246    138    https://i.ytimg.com/vi/JLQTiFwBVyI/hqdefault.jpg
        3   336    188    https://i.ytimg.com/vi/JLQTiFwBVyI/hqdefault.jpg
        4   1920   1080   https://i.ytimg.com/vi/JLQTiFwBVyI/maxresdefault.jpg
        */
        for (String output : super.outputs()) {
            if ( !Character.isDigit( output.charAt( 0 ) ) )
                continue;

            // [0, 168, 94, https://i.ytimg.com/vi/JLQTiFwBVyI/hqdefault.jpg]
            String[] parts = output.trim().split( "\\s+" );
            if ( parts.length < 4 )
                throw new InsufficientElementsException( Arrays.toString( parts ), parts.length, 4 );

            results.add( () -> parts[3] );
        }

        return () -> List.copyOf( results );
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
        public @NotNull Thumbnails build() {
            return new Thumbnails( getUrl(), getFlags(), getHeaders(), getUserAgent(), getGeoConfig() );
        }

        @Override
        public @NotNull ListResponse<Thumbnail> execute() { return this.build().execute(); }
    }
}
