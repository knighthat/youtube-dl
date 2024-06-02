package me.knighthat.youtubedl.command;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.logging.Level;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.annotations.Unmodifiable;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.experimental.Accessors;
import me.knighthat.youtubedl.YoutubeDL;
import me.knighthat.youtubedl.command.flag.Flag;
import me.knighthat.youtubedl.command.flag.GeoConfig;
import me.knighthat.youtubedl.command.flag.Header;
import me.knighthat.youtubedl.command.flag.UserAgent;
import me.knighthat.youtubedl.logging.Logger;

@Getter
@Accessors( fluent = true )
abstract class CommandImpl implements Command {

    @NotNull
    String url;
    @NotNull
    @Unmodifiable
    Set<Flag> flags;
    @NotNull
    @Unmodifiable
    Set<Header> headers;
    @Nullable
    UserAgent userAgent;
    @Nullable
    GeoConfig geoConfig;

    protected CommandImpl(
        @NotNull String url, 
        @NotNull Set<Flag> flags,
        @NotNull Set<Header> headers, 
        @Nullable UserAgent userAgent, 
        @Nullable GeoConfig geoConfig
    ) {
        this.url = url;
        this.flags = flags;
        this.headers = headers;
        this.userAgent = userAgent;
        this.geoConfig = geoConfig;
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
             .filter( arg -> !arg.isBlank() )
             .forEach( command::add );

        command.add( url );

        return command.toArray( String[]::new );
    }

    @Override
    public @NotNull @Unmodifiable Set<Flag> flags() { return Collections.unmodifiableSet( this.flags ); }

    @Override
    public @NotNull @Unmodifiable Set<Header> headers() { return Collections.unmodifiableSet( this.headers ); }

    @Getter( AccessLevel.PROTECTED )
    @Accessors( fluent = false )
    static abstract class Builder implements Command.Builder {

        private final String      url;
        private final Set<Flag>   flags;
        private final Set<Header> headers;
        private       UserAgent   userAgent;
        private       GeoConfig   geoConfig;

        protected Builder( @NotNull String url ) {
            this.url = url;
            this.flags = new HashSet<>();
            this.headers = new HashSet<>();
        }

        @Override
        public Command.@NotNull Builder flags( @NotNull Flag... flags ) {
            this.flags.addAll( Arrays.asList( flags ) );
            return this;
        }

        @Override
        public Command.@NotNull Builder headers( @NotNull Header... headers) {
            this.headers.addAll( Arrays.asList( headers ) );
            return this;
        }

        @Override
        public Command.@NotNull Builder userAgent( @Nullable UserAgent userAgent ) {
            this.userAgent = userAgent;
            return this;
        }

        @Override
        public @NotNull Builder geoConfig( @Nullable GeoConfig geoConfig ) {
            this.geoConfig = geoConfig;
            return this;
        }
    }
}