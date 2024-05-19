package me.knighthat.youtubedl.response.subtitle;

import me.knighthat.youtubedl.exception.UnsupportedSubtitleFormatException;
import org.jetbrains.annotations.NotNull;

import java.util.Locale;

/**
 * Subtitle
 */
public interface Subtitle {

    @NotNull String language();

    @NotNull Format format();

    boolean isAutomatic();

    default @NotNull Locale locale() { return new Locale( language() ); }

    public enum Format {
        VTT,
        TTML,
        SRV3,
        SRV2,
        SRV1,
        JSON3;

        public static @NotNull Format match( @NotNull String value ) {
            for (Format f : values())
                if ( f.name().equalsIgnoreCase( value ) )
                    return f;

            throw new UnsupportedSubtitleFormatException( value );
        }

        public @NotNull String extension() { return name().toLowerCase(); }
    }
}
