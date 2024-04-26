package me.knighthat.youtubedl.response.format;

import me.knighthat.youtubedl.exception.PatternMismatchException;
import org.jetbrains.annotations.NotNull;

import java.util.regex.Pattern;

interface Video {

    @NotNull
    Pattern FPS_PATTERN        = Pattern.compile( "^\\d+fps$" );
    @NotNull
    Pattern RESOLUTION_PATTERN = Pattern.compile( "^\\d+p$" );

    @NotNull String vCodec();

    default int parseFps( String @NotNull [] arr, int pos ) throws PatternMismatchException {
        if ( FPS_PATTERN.matcher( arr[pos] ).matches() ) {
            String framePerSecond = arr[pos].replace( "fps", "" );
            return Integer.parseInt( framePerSecond );
        } else
            throw new PatternMismatchException( arr[pos], "fps", arr );
    }

    default @NotNull String parseResolution( String @NotNull [] arr, int pos ) throws PatternMismatchException {
        if ( RESOLUTION_PATTERN.matcher( arr[pos] ).matches() )
            return arr[pos];
        else
            throw new PatternMismatchException( arr[pos], "resolution", arr );
    }
}
