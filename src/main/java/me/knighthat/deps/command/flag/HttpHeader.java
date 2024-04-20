package me.knighthat.deps.command.flag;

import org.jetbrains.annotations.NotNull;

import java.util.Map;

public class HttpHeader implements CommandFlag {

    public static @NotNull Value field( @NotNull String field ) { return new Builder( field ); }

    @NotNull
    private final String field;
    @NotNull
    private final String value;

    private HttpHeader( @NotNull String field, @NotNull String value ) {
        this.field = field;
        this.value = value;
    }

    @Override
    public @NotNull Map<String, String> arguments() {
        return Map.of( "--add-header", field + ":" + value );
    }

    public interface Value {
        @NotNull HttpHeader value( @NotNull String value );
    }

    private record Builder( String field ) implements Value {

        @Override
        public @NotNull HttpHeader value( @NotNull String value ) { return new HttpHeader( field, value ); }
    }
}
