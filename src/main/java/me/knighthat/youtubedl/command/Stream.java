package me.knighthat.youtubedl.command;

import me.knighthat.youtubedl.command.flag.Flag;
import me.knighthat.youtubedl.command.flag.GeoConfig;
import me.knighthat.youtubedl.command.flag.Header;
import me.knighthat.youtubedl.command.flag.UserAgent;
import me.knighthat.youtubedl.logging.Logger;
import me.knighthat.youtubedl.response.RealtimeResponse;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.IOException;
import java.io.InputStream;
import java.util.Set;
import java.util.function.BiConsumer;
import java.util.logging.Level;

public final class Stream extends Command {

    private static void stream( String @NotNull [] command, int bufferSize, @NotNull BiConsumer<byte[], Integer> stream ) throws IOException {
        Process process = new ProcessBuilder( command ).start();

        try (InputStream inStream = process.getInputStream()) {
            byte[] buffer = new byte[bufferSize];
            int bytesRead;
            while ((bytesRead = inStream.read( buffer )) != -1)
                stream.accept( buffer, bytesRead );
        }
    }

    public static @NotNull Builder builder( @NotNull String url ) { return new Builder( url ); }

    private Stream( @NotNull String url, @NotNull Set<Flag> flags, @NotNull Set<Header> headers, @Nullable UserAgent userAgent, @Nullable GeoConfig geoConfig ) {
        super( url, flags, headers, userAgent, geoConfig );
        flags().add( Flag.key( "-o" ).value( "-" ) );
    }

    @Override
    public @NotNull RealtimeResponse execute() {
        return ( bufferSize, stream ) -> {
            try {
                Stream.stream( command(), bufferSize, stream );
            } catch ( IOException e ) {
                Logger.exception( "Stream ended abruptly!", e, Level.WARNING );
            }
        };
    }

    public static class Builder extends Command.Builder {
        private Builder( @NotNull String url ) { super( url ); }

        public void stream( int bufferSize, @NotNull BiConsumer<byte[], Integer> stream ) { this.execute().stream( bufferSize, stream ); }

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
            super.setGeoConfig( geoConfig );
            return this;
        }

        @Override
        public @NotNull Stream build() { return new Stream( getUrl(), getFlags(), getHeaders(), getUserAgent(), getGeoConfig() ); }

        @Override
        public @NotNull RealtimeResponse execute() { return this.build().execute(); }
    }
}
