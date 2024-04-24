package me.knighthat.youtubedl.response.format;

import me.knighthat.youtubedl.exception.PatternMismatchException;
import org.jetbrains.annotations.NotNull;

import java.util.regex.Pattern;

interface Audio {

    @NotNull
    Pattern SAMPLING_RATE_PATTERN = Pattern.compile( "^\\d+Hz$" );

    default int parseSamplingRate( String @NotNull [] arr, int pos ) throws PatternMismatchException {
        int smplRateStart = arr[pos].indexOf( "(" ) + 1;
        int smplRateEnd = arr[pos].indexOf( ")", smplRateStart );

        String smplRate = arr[pos].substring( smplRateStart, smplRateEnd );
        if ( SAMPLING_RATE_PATTERN.matcher( smplRate ).matches() ) {
            smplRate = smplRate.substring( 0, smplRate.length() - 2 );
            return Integer.parseInt( smplRate );
        } else
            throw new PatternMismatchException( smplRate, "sampling rate", arr );
    }
}
