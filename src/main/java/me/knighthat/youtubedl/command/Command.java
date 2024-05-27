package me.knighthat.youtubedl.command;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import me.knighthat.youtubedl.YoutubeDL;
import me.knighthat.youtubedl.command.flag.Flag;
import me.knighthat.youtubedl.command.flag.GeoConfig;
import me.knighthat.youtubedl.command.flag.Header;
import me.knighthat.youtubedl.command.flag.UserAgent;
import me.knighthat.youtubedl.logging.Logger;
import me.knighthat.youtubedl.response.Response;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.*;
import java.util.logging.Level;

@Getter
@Accessors( fluent = true )
public abstract class Command {

    @NotNull
    private final String      url;
    @NotNull
    private final Set<Flag>   flags;
    @NotNull
    private final Set<Header> headers;
    @Nullable
    private final UserAgent   userAgent;
    @Nullable
    private final GeoConfig   geoConfig;

    protected Command( @NotNull String url, @NotNull Set<Flag> flags, @NotNull Set<Header> headers, @Nullable UserAgent userAgent, @Nullable GeoConfig geoConfig ) {
        this.url = url;
        this.flags = flags;
        this.headers = headers;
        this.userAgent = userAgent;
        this.geoConfig = geoConfig;
    }

    public abstract @NotNull Response execute();

    public String @NotNull [] command() {
        List<String> command = new ArrayList<>();
        command.add( YoutubeDL.getYtdlPath() );

        Set<Flag> flags = new HashSet<>();
        flags.addAll( flags() );
        flags.addAll( headers() );
        if ( userAgent() != null )
            flags.add( userAgent() );
        if ( geoConfig() != null )
            flags.add( geoConfig() );

        flags.parallelStream()
             .map( Flag::flags )
             .flatMap( Arrays::stream )
             .forEach( command::add );

        command.add( url );

        return command.toArray( String[]::new );
    }

    protected @NotNull List<String> outputs() {
        List<String> outputs = new ArrayList<>();

        try {
            Process process = new ProcessBuilder( command() ).start();

            try (BufferedReader reader = process.inputReader()) {
                String line;
                while ((line = reader.readLine()) != null)
                    outputs.add( line );
            }
        } catch ( IOException e ) {
            String command = Arrays.toString( command() );
            String message = "error occurs while executing command: " + command;
            Logger.exception( message, e, Level.SEVERE );
        }

        return outputs;
    }

    @Getter( AccessLevel.PROTECTED )
    @Setter( AccessLevel.PROTECTED )
    @Accessors( fluent = false, chain = false )
    protected static abstract class Builder {
        private final String      url;
        private final Set<Flag>   flags;
        private final Set<Header> headers;
        private       UserAgent   userAgent;
        private       GeoConfig   geoConfig;

        protected Builder( String url ) {
            this.url = url;
            this.flags = new HashSet<>();
            this.headers = new HashSet<>();
        }

        public abstract @NotNull Builder flags( @NotNull Flag... flags );

        public abstract @NotNull Builder headers( @NotNull Header... headers );

        public abstract @NotNull Builder userAgent( @Nullable UserAgent userAgent );

        public abstract @NotNull Builder geoConfig( @Nullable GeoConfig geoConfig );

        public abstract @NotNull Command build();

        public abstract @NotNull Response execute();

        protected void addFlags( @NotNull Flag... flags ) { this.flags.addAll( Arrays.asList( flags ) ); }

        protected void addHeaders( @NotNull Header... headers ) { this.headers.addAll( Arrays.asList( headers ) ); }
    }
}
