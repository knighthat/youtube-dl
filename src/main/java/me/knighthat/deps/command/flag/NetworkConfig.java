package me.knighthat.deps.command.flag;

import lombok.Builder;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;

@Builder
public class NetworkConfig implements CommandFlag {

    @NotNull
    @Builder.Default
    private String  proxyUrl = "";
    @Builder.Default
    private int     timeout  = 0;
    @NotNull
    @Builder.Default
    private String  source   = "";
    @Builder.Default
    private boolean forceV4  = false;
    @Builder.Default
    private boolean forceV6  = false;

    @Override
    public @NotNull Map<String, String> arguments() {
        if ( forceV4 && forceV6 )
            throw new IllegalStateException( "Cannot use both IPv4 and IPv6 at the same time!" );

        Map<String, String> results = new HashMap<>();

        if ( !proxyUrl.isBlank() )
            results.put( "--proxy", proxyUrl );
        if ( timeout > 0 )
            results.put( "--socket-timeout", String.valueOf( timeout ) );
        if ( !source.isBlank() )
            results.put( "--source-address", source );
        if ( forceV4 )
            results.put( "-4", "" );
        if ( forceV6 )
            results.put( "-6", "" );

        return results;
    }
}
