package me.knighthat.youtubedl.command.flag;

import org.jetbrains.annotations.NotNull;

import java.util.Map;

public interface CommandFlag {

    static @NotNull Builder key( @NotNull String key ) { return new Builder( key ); }

    static @NotNull CommandFlag noValue( @NotNull String key ) { return new Builder( key ).build(); }

    @NotNull Map<String, String> arguments();

    class Builder {

        private final String key;
        private       String value;

        private Builder( @NotNull String key ) {
            this.key = key;
            this.value = "";
        }

        public @NotNull CommandFlag value( @NotNull String value ) {
            if ( !this.value.isBlank() )
                throw new IllegalAccessError( "value already set!" );

            this.value = value;
            return build();
        }

        public @NotNull CommandFlag build() { return () -> Map.of( key, value ); }
    }
}
