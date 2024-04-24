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

import java.io.IOException;
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
        List<CommandFlag> arguments = new ArrayList<>( argSize );

        arguments.addAll( this.headers );
        arguments.addAll( this.flags );
        arguments.add( this.userAgent );
        if ( this.geoConfig != null )
            arguments.add( this.geoConfig );

        List<String> result = new ArrayList<>( YoutubeDL.getCommand().length + argSize );

        result.addAll( List.of( YoutubeDL.getCommand() ) );
        arguments.forEach( arg -> {
            for (Map.Entry<String, String> entry : arg.arguments().entrySet()) {
                result.add( entry.getKey() );
                if ( !entry.getValue().isBlank() )
                    result.add( entry.getValue() );
            }
        } );
        result.add( this.url );

        return result.toArray( String[]::new );
    }

    protected @NotNull List<String> execute0() throws IOException, InterruptedException {
        return Command.captureOutput( command() );
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
