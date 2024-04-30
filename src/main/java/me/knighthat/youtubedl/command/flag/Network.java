package me.knighthat.youtubedl.command.flag;

import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;

public class Network implements Flag {

    public static @NotNull Builder builder() { return new Builder(); }

    @NotNull
    private final String  proxy;
    private final int     timeout;
    @NotNull
    private final String  source;
    private final boolean forceIPv4;
    private final boolean forceIPv6;

    private Network( @NotNull String proxy, int timeout, @NotNull String source, boolean forceIPv4, boolean forceIPv6 ) {
        this.proxy = proxy;
        this.timeout = timeout;
        this.source = source;
        this.forceIPv4 = forceIPv4;
        this.forceIPv6 = forceIPv6;
    }

    @Override
    public String @NotNull [] flags() {
        Map<String, String> results = new HashMap<>();

        if ( !proxy.isBlank() )
            results.put( "--proxy", proxy );
        if ( timeout > 0 )
            results.put( "--socket-timeout", String.valueOf( timeout ) );
        if ( !source.isBlank() )
            results.put( "--source-address", source );
        if ( forceIPv4 )
            results.put( "-4", "" );
        if ( forceIPv6 )
            results.put( "-6", "" );

        return FlagUtils.mapToArray( results );
    }

    public static class Builder {
        private String  proxy     = "";
        private int     timeout   = 0;
        private String  source    = "";
        private boolean forceIPv4 = false;
        private boolean forceIPv6 = false;

        private Builder() { }

        public @NotNull Builder proxy( @NotNull String url ) {
            this.proxy = url;
            return this;
        }

        public @NotNull Builder timeout( int timeout ) {
            if ( timeout < 0 )
                throw new IllegalArgumentException( "'timeout' must be a positive number!" );
            else
                this.timeout = timeout;
            return this;
        }

        public @NotNull Builder sourceIp( @NotNull String address ) {
            // Verify input 'address' is valid IPv4
            String[] octets = address.split( "\\." );
            boolean throwError = false;
            for (int i = 0 ; i < octets.length ; i++) {
                if ( i > 3 )
                    throwError = true;
                if ( throwError )
                    break;

                int octet = Integer.parseInt( octets[i] );
                throwError = octet < 0 || octet > 255;
            }

            if ( throwError )
                throw new IllegalArgumentException( "Invalid ip address: " + address );

            this.source = address;
            return this;
        }

        public @NotNull Builder forceIPv4() {
            if ( forceIPv6 )
                throw new IllegalArgumentException( "Choose one! force IPv4 or force IPv6" );
            else
                this.forceIPv4 = true;
            return this;
        }

        public @NotNull Builder forceIPv6() {
            if ( forceIPv4 )
                throw new IllegalArgumentException( "Choose one! force IPv4 or force IPv6" );
            else
                this.forceIPv6 = true;
            return this;
        }

        public @NotNull Network build() {
            return new Network( proxy, timeout, source, forceIPv4, forceIPv6 );
        }
    }
}
