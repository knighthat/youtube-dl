package me.knighthat.deps.command;

import me.knighthat.deps.YoutubeDL;
import me.knighthat.deps.command.flag.CommandFlag;
import me.knighthat.deps.command.flag.GeoConfig;
import me.knighthat.deps.command.flag.HttpHeader;
import me.knighthat.deps.command.flag.UserAgent;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.annotations.Unmodifiable;

import java.io.IOException;
import java.util.*;

public abstract class YtdlCommand {

    @NotNull
    private final String           url;
    @NotNull
    private final Set<CommandFlag> flags;
    @NotNull
    private final Set<HttpHeader>  headers;

    @NotNull
    private UserAgent userAgent = UserAgent.FIREFOX_WINDOWS;
    @Nullable
    private GeoConfig geoConfig = null;

    public YtdlCommand( @NotNull String url ) {
        this.url = url;
        this.flags = new HashSet<>();
        this.headers = new HashSet<>();
    }

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

    public @NotNull String url() { return this.url; }

    public @NotNull @Unmodifiable Set<CommandFlag> flags() { return Set.copyOf( flags ); }

    public void flags( @NotNull CommandFlag... flags ) { this.flags.addAll( Arrays.asList( flags ) ); }

    public @NotNull @Unmodifiable Set<HttpHeader> headers() { return Set.copyOf( headers ); }

    public void headers( @NotNull HttpHeader... headers ) { this.headers.addAll( Arrays.asList( headers ) ); }

    public @NotNull UserAgent userAgent() { return this.userAgent; }

    public void userAgent( @NotNull UserAgent userAgent ) { this.userAgent = userAgent; }

    public @Nullable GeoConfig geoConfig() { return this.geoConfig; }

    public void geoConfig( @NotNull GeoConfig geoConfig ) { this.geoConfig = geoConfig; }
}
