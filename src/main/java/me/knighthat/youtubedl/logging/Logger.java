package me.knighthat.youtubedl.logging;

import org.jetbrains.annotations.NotNull;

import java.util.logging.Level;

public class Logger {

    @NotNull
    private static final java.util.logging.Logger LOGGER;

    static {
        LOGGER = java.util.logging.Logger.getLogger( "YoutubeDL Wrapper" );
    }

    public static void exception( @NotNull String message, @NotNull Throwable throwable, @NotNull Level level ) {
        LOGGER.log( level, message );

        String reason = throwable.getMessage();
        if ( reason != null && !reason.isBlank() )
            LOGGER.log( level, "Reason: " + reason );

        Throwable cause = throwable.getCause();
        if ( cause == null )
            return;

        String causeReason = cause.getMessage();
        if ( causeReason != null && !causeReason.isBlank() )
            LOGGER.log( level, "Caused by: " + causeReason );
    }

    public static void warning( @NotNull String s ) { LOGGER.warning( s ); }
}
