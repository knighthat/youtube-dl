package me.knighthat.youtubedl.command.flag;

import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;

public class Header implements Flag {

    public static @NotNull Builder key( @NotNull String key ) { return new Builder( key ); }

    public static @NotNull Chain chain() { return new Chain(); }

    private final @NotNull Map<String, String> headers;

    private Header( @NotNull Map<String, String> headers ) { this.headers = headers; }

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
        @NotNull Value key( @NotNull String key );

        @NotNull Header build();
    }

    public interface Value {
        @NotNull Key value( @NotNull String value );
    }

    public static class Builder extends Flag.Builder {
        private Builder( @NotNull String key ) { super( key ); }

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
