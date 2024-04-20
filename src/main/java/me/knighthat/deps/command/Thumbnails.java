package me.knighthat.deps.command;

import me.knighthat.deps.command.flag.CommandFlag;
import me.knighthat.deps.command.flag.GeoConfig;
import me.knighthat.deps.command.flag.HttpHeader;
import me.knighthat.deps.command.flag.UserAgent;
import me.knighthat.deps.exception.InsufficientElementsException;
import me.knighthat.deps.response.ListResponse;
import me.knighthat.deps.response.thumbnail.Thumbnail;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.IOException;
import java.util.*;

public class Thumbnails extends YtdlCommand {

    public static @NotNull Builder builder( @NotNull String url ) {
        return new Builder( url ).flags( () -> Map.of( "--list-thumbnails", "" ) );
    }

    private Thumbnails( @NotNull String url, @NotNull Set<CommandFlag> flags, @NotNull Set<HttpHeader> headers, @NotNull UserAgent userAgent, @Nullable GeoConfig geoConfig ) {
        super( url, flags, headers, userAgent, geoConfig );
    }

    @Override
    public @NotNull Results execute() {
        List<Thumbnail> responseFormats = new ArrayList<>();
        List<String> outputs = new ArrayList<>();
        try {
            outputs = super.execute0();
        } catch ( IOException | InterruptedException ignored ) {
        }

        /*
            0   168    94     https://i.ytimg.com/vi/JLQTiFwBVyI/hqdefault.jpg
            1   196    110    https://i.ytimg.com/vi/JLQTiFwBVyI/hqdefault.jpg
            2   246    138    https://i.ytimg.com/vi/JLQTiFwBVyI/hqdefault.jpg
            3   336    188    https://i.ytimg.com/vi/JLQTiFwBVyI/hqdefault.jpg
            4   1920   1080   https://i.ytimg.com/vi/JLQTiFwBVyI/maxresdefault.jpg
        */
        for (String output : outputs) {
            if ( !Character.isDigit( output.charAt( 0 ) ) )
                continue;

            String[] parts = output.trim().split( "\\s+" );
            if ( parts.length < 4 )
                throw new InsufficientElementsException( "[" + output + "]", parts.length, 4 );

            Thumbnail thumbnail = new Thumbnail( Integer.parseInt( parts[1] ), Integer.parseInt( parts[2] ), parts[3] );
            responseFormats.add( thumbnail );
        }

        return new Results( responseFormats );
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
        public @NotNull Thumbnails build() {
            return new Thumbnails( url(), flags(), headers(), userAgent(), geoConfig() );
        }

        @Override
        public @NotNull Results execute() {
            return this.build().execute();
        }
    }

    public record Results( @NotNull List<Thumbnail> items ) implements ListResponse<Thumbnail> {
    }
}
