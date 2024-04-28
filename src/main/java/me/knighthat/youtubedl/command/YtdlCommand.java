package me.knighthat.youtubedl.command;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import me.knighthat.youtubedl.YoutubeDL;
import me.knighthat.youtubedl.command.flag.CommandFlag;
import me.knighthat.youtubedl.command.flag.GeoConfig;
import me.knighthat.youtubedl.command.flag.HttpHeader;
import me.knighthat.youtubedl.command.flag.UserAgent;
import me.knighthat.youtubedl.response.Response;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.*;

public abstract class YtdlCommand {

    @NotNull
    protected final String           url;
    @NotNull
    protected final Set<CommandFlag> flags;
    @NotNull
    protected final Set<HttpHeader>  headers;
    @NotNull
    protected final UserAgent        userAgent;
    @Nullable
    protected final GeoConfig        geoConfig;

    YtdlCommand( @NotNull String url, @NotNull Set<CommandFlag> flags, @NotNull Set<HttpHeader> headers, @NotNull UserAgent userAgent, @Nullable GeoConfig geoConfig ) {
        this.url = url;
        this.flags = flags;
        this.headers = headers;
        this.userAgent = userAgent;
        this.geoConfig = geoConfig;
    }

    public abstract @NotNull Response execute();

    protected String @NotNull [] command() {
        // UserAgent and GeoConfig take 2 elements;
        int argSize = headers.size() + flags.size() + 2;
        Set<CommandFlag> flags = new HashSet<>( argSize );
        flags.addAll( this.headers );
        flags.addAll( this.flags );
        flags.add( this.userAgent );
        if ( this.geoConfig != null )
            flags.add( this.geoConfig );

        // 3 is reserved spaces for 'python', 'youtube-dl' and url
        int totalSize = 2 + argSize;
        List<String> command = new ArrayList<>( totalSize );

        command.add( YoutubeDL.getPythonPath() );
        command.add( YoutubeDL.getYtdlPath() );
        flags.forEach( f -> {
            for (Map.Entry<String, String> entry : f.arguments().entrySet()) {
                command.add( entry.getKey() );
                if ( !entry.getValue().isBlank() )
                    command.add( entry.getValue() );
            }
        } );
        command.add( url );

        return command.toArray( String[]::new );
    }

    protected @NotNull List<String> execute0() throws IOException, InterruptedException {
        List<String> outputs = new ArrayList<>();

        Process process = new ProcessBuilder( command() ).start();
        try (
                InputStreamReader reader = new InputStreamReader( process.getInputStream() ) ;
                BufferedReader bReader = new BufferedReader( reader )
        ) {
            String line;
            while ((line = bReader.readLine()) != null)
                outputs.add( line );
        }

        return outputs;
    }

    @Getter( AccessLevel.PUBLIC )
    @Accessors( chain = true, fluent = true )
    protected static abstract class Builder {

        private final String           url;
        private final Set<CommandFlag> flags   = new HashSet<>();
        private final Set<HttpHeader>  headers = new HashSet<>();

        @Setter( AccessLevel.PROTECTED )
        private UserAgent userAgent = UserAgent.FIREFOX_WINDOWS;
        @Setter( AccessLevel.PROTECTED )
        private GeoConfig geoConfig = null;

        Builder( @NotNull String url ) { this.url = url; }

        public abstract @NotNull YtdlCommand build();

        public @NotNull Response execute() { return build().execute(); }
    }
}
