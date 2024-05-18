package me.knighthat.youtubedl.command.flag;

import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;

/**
 * All configuration classes derives from this class.
 * IT provides simple to wait to construct
 * arguments for your final command.
 */
public interface Flag {

    /**
     * Initialize single key/value pair flag.
     * 
     * @param key name of the flag, usually begins with 1 or 2 dashes.
     * 
     * @return {@link me.knighthat.youtubedl.command.flag.Flag.Value} that allows value of the key to be typed in
     */
    static @NotNull Builder key( @NotNull String key ) { return new Builder( key ); }

    /**
     * Initialize no value switch.
     * 
     * @param key name of the switch
     * 
     * @return an instance of {@link me.knighthat.youtubedl.command.flag.Flag} that only contains key and no value
     */
    static @NotNull Flag noValue( @NotNull String key ) { return Flag.key( key ).value( "" ); }

    /**
     * Let you combine multiple key/value pairs together
     * into 1 single instance of {@link me.knighthat.youtubedl.command.flag.Flag}.
     * <p>
     * Start with {@link me.knighthat.youtubedl.command.flag.Flag.Key}, then it
     * will move to {@link me.knighthat.youtubedl.command.flag.Flag.Value}.
     * The process keeps repeating until {@link me.knighthat.youtubedl.command.flag.Flag.Key#build()} is called.
     * <p>
     * Note: {@link me.knighthat.youtubedl.command.flag.Flag.Key#build()} only available after both key and value are provided.
     * 
     * @return the beginning of the chain.
     */
    static @NotNull Key chain() { return new Chain(); }

    /**
     * @return an array of arguments following [key, value] format
     */
    String @NotNull [] flags();

    interface Key {
        /**
         * @param key name of the flag
         * 
         * @return an instance that accepts value to complete key/value pair
         */
        @NotNull Value key( @NotNull String key );

        /**
         * Finalize the configuration and pack it in {@link me.knighthat.youtubedl.command.flag.Flag} class.
         * 
         * @return finalized {@link me.knighthat.youtubedl.command.flag.Flag} class 
         */
        @NotNull Flag build();
    }

    interface Value {
        /**
         * @param value value of the flag
         * 
         * @return an instance that accepts new key or provide build method
         */
        @NotNull Key value( @NotNull String value );
    }

    class Builder {
        protected final String key;

        Builder( @NotNull String key ) { this.key = key; }

        /**
         * Finalize the configuration and pack it in {@link me.knighthat.youtubedl.command.flag.Flag} class.
         * 
         * @return finalized {@link me.knighthat.youtubedl.command.flag.Flag} class 
         */
        public @NotNull Flag value( @NotNull String value ) { return () -> new String[]{ this.key, value }; }
    }

    class Chain implements Key, Value {
        protected final Map<String, String> pairs;
        protected       String              key;

        Chain() { this.pairs = new HashMap<>(); }

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
        public @NotNull Flag build() { return () -> FlagUtils.mapToArray( this.pairs ); }
    }
}
