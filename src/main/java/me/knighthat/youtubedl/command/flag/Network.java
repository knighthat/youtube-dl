package me.knighthat.youtubedl.command.flag;

import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;

/**
 * A set of network configurations that allows user to
 * modify how youtube-dl interacts with the other server.
 * <p>
 * For example, if you want to talk to the server through
 * a proxy server, or connect through IPv6, then this
 * is the right place to be
 */
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

    /**
     * An array represents the network configurations
     * that will be sent along with the request.
     * 
     * @return an array of arguments following [key, value] format
     */
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

        /**
         * Route traffic through a proxy server
         * <p>
         * "url" should follow standard url format
         * scheme://sub.domain.top:port.
         * E.g. socks5://example.org:1234
         * <p>
         * Note: This method overrides the value 
         * of the same method called previously.
         * 
         * @param url the ip address of the proxy server
         * 
         * @return same builder instance with updated value
         */
        public @NotNull Builder proxy( @NotNull String url ) {
            this.proxy = url;
            return this;
        }

        /**
         * Set how long should program wait for the
         * response from the server.
         * By default, this number is 600.
         * <p>
         * Note: This method overrides the value 
         * of the same method called previously.
         * 
         * @param seconds how long (in seconds) that youtube-dl should wait before giving up
         * 
         * @return same builder instance with updated value
         * 
         * @throws IllegalArgumentException when provided number is below zero
         */
        public @NotNull Builder timeout( int seconds ) {
            if ( seconds < 0 )
                throw new IllegalArgumentException( "'timeout' must be a positive number!" );
            else
                this.timeout = seconds;
            return this;
        }

        /**
         * Bind sending traffic to a specific network interface.
         * <p>
         * IP address must follow conventional IPv4 format, which
         * consists of 4 octets with each octet is a number ranging
         * from 0 to 255.
         * <p>
         * Note: This method overrides the value 
         * of the same method called previously.
         * 
         * @param address interface's IPv4 address
         * 
         * @return same builder instance with updated value
         * 
         * @throws IllegalArgumentException when address is invalid
         */
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

        /**
         * Instructs program to communicate with the server using IPv4
         * <p>
         * Note: This argument can only be turned on. 
         * Adding multiple calls will not change the outcome.
         * 
         * @return same builder instance with updated value
         * 
         * @throws IllegalArgumentException when force IPv6 is already enabled
         */
        public @NotNull Builder forceIPv4() {
            if ( forceIPv6 )
                throw new IllegalArgumentException( "Choose one! force IPv4 or force IPv6" );
            else
                this.forceIPv4 = true;
            return this;
        }

        /**
         * Instructs program to communicate with the server using IPv6
         * <p>
         * Note: This argument can only be turned on. 
         * Adding multiple calls will not change the outcome.
         * 
         * @return same builder instance with updated value
         * 
         * @throws IllegalArgumentException when force IPv4 is already enabled
         */
        public @NotNull Builder forceIPv6() {
            if ( forceIPv4 )
                throw new IllegalArgumentException( "Choose one! force IPv4 or force IPv6" );
            else
                this.forceIPv6 = true;
            return this;
        }

        /**
         * Finalize the configuration and pack it in {@link me.knighthat.youtubedl.command.flag.Network} class.
         * 
         * @return finalized {@link me.knighthat.youtubedl.command.flag.Network} class 
         */
        public @NotNull Network build() { return new Network( proxy, timeout, source, forceIPv4, forceIPv6 ); }
    }
}
