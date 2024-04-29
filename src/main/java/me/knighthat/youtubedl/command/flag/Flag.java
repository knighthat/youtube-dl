package me.knighthat.youtubedl.command.flag;

import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;

public interface Flag {

    static @NotNull Builder key( @NotNull String key ) { return new Builder( key ); }

    static @NotNull Flag noValue( @NotNull String key ) { return Flag.key( key ).value( "" ); }

    static @NotNull Key chain() { return new Chain(); }

    String @NotNull [] flags();

    interface Key {
        @NotNull Value key( @NotNull String key );

        @NotNull Flag build();
    }

    interface Value {
        @NotNull Key value( @NotNull String value );
    }

    class Builder {
        protected final String key;

        Builder( @NotNull String key ) { this.key = key; }

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
