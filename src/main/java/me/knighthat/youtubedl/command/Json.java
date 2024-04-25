package me.knighthat.youtubedl.command;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonNull;
import com.google.gson.JsonObject;
import me.knighthat.youtubedl.command.flag.CommandFlag;
import me.knighthat.youtubedl.command.flag.GeoConfig;
import me.knighthat.youtubedl.command.flag.HttpHeader;
import me.knighthat.youtubedl.command.flag.UserAgent;
import me.knighthat.youtubedl.response.Response;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.Map;
import java.util.Set;

public class Json extends YtdlCommand {

    @NotNull
    private static final Gson GSON = new Gson();

    public static @NotNull Builder builder( @NotNull String url ) { return new Builder( url ).flags( () -> Map.of( "-j", "" ) ); }

    private Json( @NotNull String url, @NotNull Set<CommandFlag> flags, @NotNull Set<HttpHeader> headers, @NotNull UserAgent userAgent, @Nullable GeoConfig geoConfig ) {
        super( url, flags, headers, userAgent, geoConfig );
    }

    @Override
    public @NotNull JsonResult execute() {
        StringBuilder builder = new StringBuilder();

        JsonElement result = JsonNull.INSTANCE;
        try {
            Process process = new ProcessBuilder( command() ).start();
            InputStream inStream = process.getInputStream();
            byte[] buffer = new byte[1024];
            int bytesRead;
            while ((bytesRead = inStream.read( buffer )) != -1) {
                String partial = new String( buffer, 0, bytesRead );
                builder.append( partial );
            }
            inStream.close();

            if ( process.waitFor() == 0 && !builder.isEmpty() )
                result = GSON.fromJson( builder.toString(), JsonObject.class );
        } catch ( IOException | InterruptedException ignored ) {
        }

        return new JsonResult( result );
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
        public @NotNull Json build() { return new Json( url(), flags(), headers(), userAgent(), geoConfig() ); }

        @Override
        public @NotNull JsonResult execute() { return this.build().execute(); }
    }

    public record JsonResult( @NotNull JsonElement json ) implements Response {
    }
}
