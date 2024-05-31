package me.knighthat.youtubedl.command;

import java.io.IOException;
import java.io.InputStream;
import java.util.Set;
import java.util.function.BiConsumer;
import java.util.logging.Level;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import me.knighthat.youtubedl.command.flag.Flag;
import me.knighthat.youtubedl.command.flag.GeoConfig;
import me.knighthat.youtubedl.command.flag.Header;
import me.knighthat.youtubedl.command.flag.UserAgent;
import me.knighthat.youtubedl.logging.Logger;
import me.knighthat.youtubedl.response.RealtimeResponse;

/**
 * StreamImpl
 */
public class StreamImpl extends CommandImpl implements Stream {

    private static void stream( String @NotNull [] command, int bufferSize, @NotNull BiConsumer<byte[], Integer> stream ) throws IOException {
        Process process = new ProcessBuilder( command ).start();

        try (InputStream inStream = process.getInputStream()) {
            byte[] buffer = new byte[bufferSize];
            int bytesRead;
            while ((bytesRead = inStream.read( buffer )) != -1)
                stream.accept( buffer, bytesRead );
        }
    }

    public static @NotNull Stream.Builder builder( @NotNull String url ) { return new Builder( url ); }

    protected StreamImpl(
        @NotNull String url, 
        @NotNull Set<Flag> flags, 
        @NotNull Set<Header> headers,
        @Nullable UserAgent userAgent, 
        @Nullable GeoConfig geoConfig
    ) {
        super(url, flags, headers, userAgent, geoConfig);
        flags.add( Flag.key( "-o" ).value( "-" ) );
    }

    @Override
    public @NotNull RealtimeResponse execute() {
        return ( bufferSize, stream ) -> {
            try {
                StreamImpl.stream( command(), bufferSize, stream );
            } catch ( IOException e ) {
                Logger.exception( "Stream ended abruptly!", e, Level.WARNING );
            }
        };
    }

    public static class Builder extends CommandImpl.Builder implements Stream.Builder  {

        protected Builder(String url) { super(url); }

        @Override
        public @NotNull Builder flags( @NotNull Flag... flags ) { return (Builder) super.flags( flags ); }

        @Override
        public @NotNull Builder headers( @NotNull Header... headers) { return (Builder) super.headers( headers ); }

        @Override
        public @NotNull Builder userAgent( @Nullable UserAgent userAgent ) { return (Builder) super.userAgent( userAgent ); }

        @Override
        public @NotNull Builder geoConfig( @Nullable GeoConfig geoConfig ) { return (Builder) super.geoConfig( geoConfig ); }

        @Override
        public @NotNull Stream build() {
            return new StreamImpl( getUrl(), getFlags(), getHeaders(), getUserAgent(), getGeoConfig() );
        }

        @Override
        public @NotNull RealtimeResponse execute() { return this.build().execute(); } }    
}