package me.knighthat.youtubedl.command;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonNull;
import com.google.gson.JsonObject;
import me.knighthat.youtubedl.command.flag.Flag;
import me.knighthat.youtubedl.command.flag.GeoConfig;
import me.knighthat.youtubedl.command.flag.Header;
import me.knighthat.youtubedl.command.flag.UserAgent;
import me.knighthat.youtubedl.logging.Logger;
import me.knighthat.youtubedl.response.SingleResultResponse;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.IOException;
import java.io.InputStream;
import java.util.Set;
import java.util.logging.Level;

public final class Json extends Command {

    @NotNull
    static final Gson GSON = new Gson();

    public static @NotNull Builder builder( @NotNull String url ) { return new Builder( url ); }

    private Json( @NotNull String url, @NotNull Set<Flag> flags, @NotNull Set<Header> headers, @Nullable UserAgent userAgent, @Nullable GeoConfig geoConfig ) {
        super( url, flags, headers, userAgent, geoConfig );
        flags().add( Flag.noValue( "--dump-json" ) );
    }

    @Override
    public @NotNull SingleResultResponse<JsonElement> execute() {
        try {
            StringBuilder builder = new StringBuilder();

            Process process = new ProcessBuilder( command() ).start();
            try (InputStream inStream = process.getInputStream()) {
                byte[] buffer = new byte[1024];
                int bytesRead;
                while ((bytesRead = inStream.read( buffer )) != -1) {
                    String partial = new String( buffer, 0, bytesRead );
                    builder.append( partial );
                }
            }

            if ( process.waitFor() == 0 && !builder.isEmpty() )
                return () -> GSON.fromJson( builder.toString(), JsonObject.class );
        } catch ( IOException | InterruptedException e ) {
            String message;
            if ( e instanceof IOException )
                message = "could not convert object to JsonObject!";
            else
                message = "thread interrupted while converting object to JsonObject!";

            Logger.exception( message, e, Level.WARNING );
        }

        return () -> JsonNull.INSTANCE;
    }

    public static class Builder extends Command.Builder {
        private Builder( @NotNull String url ) { super( url ); }

        @Override
        public @NotNull Builder flags( @NotNull Flag... flags ) { return (Builder) super.flags( flags ); }

        @Override
        public @NotNull Builder headers( @NotNull Header... headers ) { return (Builder) super.headers( headers ); }

        @Override
        public @NotNull Builder userAgent( @Nullable UserAgent userAgent ) { return (Builder) super.userAgent( userAgent ); }

        @Override
        public @NotNull Builder geoConfig( @Nullable GeoConfig geoConfig ) { return (Builder) super.geoConfig( geoConfig ); }

        @Override
        public @NotNull Json build() { 
            return new Json( getUrl(), getFlags(), getHeaders(), getUserAgent(), getGeoConfig() ); 
        }

        @Override
        public @NotNull SingleResultResponse<JsonElement> execute() { return this.build().execute(); }
    }
}
