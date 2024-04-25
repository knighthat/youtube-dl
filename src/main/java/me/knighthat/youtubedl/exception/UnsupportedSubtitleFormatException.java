package me.knighthat.youtubedl.exception;

import org.jetbrains.annotations.NotNull;

import java.io.Serial;

public class UnsupportedSubtitleFormatException extends IllegalStateException {

    @Serial
    private static final long serialVersionUID = 1948572647384756285L;

    public UnsupportedSubtitleFormatException( @NotNull String extension ) {
        super( "Unrecognized subtitle format \"" + extension + "\"" );
    }
}
