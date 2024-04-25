package me.knighthat.youtubedl.command;

import me.knighthat.youtubedl.command.flag.CommandFlag;
import me.knighthat.youtubedl.command.flag.GeoConfig;
import me.knighthat.youtubedl.command.flag.HttpHeader;
import me.knighthat.youtubedl.command.flag.UserAgent;
import me.knighthat.youtubedl.response.Response;
import org.apache.commons.lang3.NotImplementedException;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.Map;
import java.util.Set;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

public class Stream extends YtdlCommand {

    public static @NotNull Builder builder( @NotNull String url ) { return new Builder( url ).flags( () -> Map.of( "-o", "-" ) ); }

    private Stream( @NotNull String url, @NotNull Set<CommandFlag> flags, @NotNull Set<HttpHeader> headers, @NotNull UserAgent userAgent, @Nullable GeoConfig geoConfig ) {
        super( url, flags, headers, userAgent, geoConfig );
    }

    @Override
    public @NotNull Result execute() {
        throw new NotImplementedException();
    }

    public interface Result extends Response {
        void stream( @NotNull Consumer<InputStream> stream );
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

        public void stream( int bufferSize, @NotNull BiConsumer<byte[], Integer> stream ) throws IOException, InterruptedException {
            Process process = new ProcessBuilder( build().command() ).start();

            InputStream inStream = process.getInputStream();
            byte[] buffer = new byte[bufferSize];
            int bytesRead;
            while ((bytesRead = inStream.read( buffer )) != -1)
                stream.accept( buffer, bytesRead );

            inStream.close();
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
        public @NotNull Stream build() {
            return new Stream( url(), flags(), headers(), userAgent(), geoConfig() );
        }

        @Override
        public @NotNull Result execute() {
            throw new NotImplementedException();
        }
    }
}
