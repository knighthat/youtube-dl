package me.knighthat.youtubedl.command.flag;

import org.jetbrains.annotations.NotNull;

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

    public static class Builder extends Flag.Builder {
        private Builder( @NotNull String key ) { super( key ); }

        @Override
        public @NotNull Header value( @NotNull String value ) { return new Header( Map.of( super.key, value ) ); }
    }

    public static class Chain extends Flag.Chain {
        private Chain() { super(); }

        @Override
        public @NotNull Header build() { return new Header( super.pairs ); }
    }
}
