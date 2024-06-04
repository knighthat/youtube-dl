package me.knighthat.youtubedl.command.flag;

import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;

/**
 * A set of configurations that represents
 * the header of the request.
 * <p>
 * Header of a request can be necessary for
 * the server to be able to complete the request.
 * <p>
 * Checkout: <a href="https://developer.mozilla.org/en-US/docs/Web/HTTP/Headers">Mozilla: Headers</a>
 */
public class Header implements Flag {

    /**
     * Initialize single key/value pair header.
     *
     * @param key name of the header.
     *
     * @return {@link me.knighthat.youtubedl.command.flag.Header.Value} that allows value of the key to be typed in
     */
    public static @NotNull Builder key( @NotNull String key ) { return new Builder( key ); }

    /**
     * Let you combine multiple key/value pairs together
     * into 1 single instance of {@link me.knighthat.youtubedl.command.flag.Header}.
     * <p>
     * Start with {@link me.knighthat.youtubedl.command.flag.Header.Key}, then it
     * will move to {@link me.knighthat.youtubedl.command.flag.Header.Value}.
     * The process keeps repeating until {@link me.knighthat.youtubedl.command.flag.Header.Key#build()} is called.
     * <p>
     * Note: {@link me.knighthat.youtubedl.command.flag.Header.Key#build()} only available after both key and value are provided.
     *
     * @return the beginning of the chain.
     */
    public static @NotNull Chain chain() { return new Chain(); }

    private final @NotNull Map<String, String> headers;

    private Header( @NotNull Map<String, String> headers ) { this.headers = headers; }

    /**
     * An array represents the headers
     * that will be sent along with the request.
     *
     * @return an array of arguments following [key, value] format
     */
    @Override
    public String @NotNull [] flags() {
        String[] results = new String[headers.size() * 2];

        int index = 0;
        for (Map.Entry<String, String> entry : headers.entrySet()) {
            results[index++] = "--add-header";
            results[index++] = "%s:%s".formatted( entry.getKey(), entry.getValue() );
        }

        return results;
    }

    public interface Key {
        /**
         * @param key name of the header
         *
         * @return an instance that accepts value to complete key/value pair
         */
        @NotNull Value key( @NotNull String key );

        /**
         * Finalize the configuration and pack it in {@link me.knighthat.youtubedl.command.flag.Header} class.
         *
         * @return finalized {@link me.knighthat.youtubedl.command.flag.Header} class
         */
        @NotNull Header build();
    }

    public interface Value {
        /**
         * @param value value of the header
         *
         * @return an instance that accepts new key or provide build method
         */
        @NotNull Key value( @NotNull String value );
    }

    public static class Builder extends Flag.Builder {
        private Builder( @NotNull String key ) { super( key ); }

        /**
         * Finalize the configuration and pack it in {@link me.knighthat.youtubedl.command.flag.Header} class.
         *
         * @return finalized {@link me.knighthat.youtubedl.command.flag.Header} class
         */
        @Override
        public @NotNull Header value( @NotNull String value ) { return new Header( Map.of( super.key, value ) ); }
    }

    public static class Chain implements Key, Value {
        private final Map<String, String> pairs;
        private       String              key;

        private Chain() { this.pairs = new HashMap<>(); }

        @Override
        public @NotNull Key value( @NotNull String value ) {
            pairs.put( key, value );
            return this;
        }

        @Override
        public @NotNull Value key( @NotNull String key ) {
            if ( key.isEmpty() )
                throw new IllegalArgumentException( "empty key!" );
            else
                this.key = key;
            return this;
        }

        @Override
        public @NotNull Header build() { return new Header( this.pairs ); }
    }
}
