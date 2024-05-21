package me.knighthat.extractor.youtube.response.format;

import java.math.BigInteger;
import java.util.regex.Pattern;

import org.jetbrains.annotations.NotNull;

import com.google.gson.Gson;

import me.knighthat.youtubedl.exception.PatternMismatchException;

/**
 * FormatUtils
 */
class FormatUtils {

    @NotNull
    private static String SIZE_FORMAT = "^\\d+(\\.\\d+)?";
    @NotNull
    private static String SIZE_UNIT = "[KMGTPEZY]?iB$";

    @NotNull
    static final Pattern FPS_PATTERN        = Pattern.compile( "^\\d+fps$" );
    @NotNull
    static final Pattern RESOLUTION_PATTERN = Pattern.compile( "^\\d+p$" );
    @NotNull
    static final Pattern QUALITY_PATTERN = Pattern.compile( "^\\d+k$" );
    @NotNull
    static final Pattern SIZE_PATTERN = Pattern.compile( SIZE_FORMAT + SIZE_UNIT );
    @NotNull
    static final Pattern SAMPLING_RATE_PATTERN = Pattern.compile( "^\\d+Hz$" );

    @NotNull
    static final Gson GSON = new Gson();

    static float parseFps( String @NotNull [] arr, int pos ) throws PatternMismatchException {
        if ( FPS_PATTERN.matcher( arr[pos] ).matches() ) {
            String framePerSecond = arr[pos].replace( "fps", "" );
            return Float.parseFloat( framePerSecond );
        } else
            throw new PatternMismatchException( arr[pos], "fps", arr );
    }

    static @NotNull String parseResolution( String @NotNull [] arr, int pos ) throws PatternMismatchException {
        if ( RESOLUTION_PATTERN.matcher( arr[pos] ).matches() )
            return arr[pos];
        else
            throw new PatternMismatchException( arr[pos], "resolution", arr );
    }

    static @NotNull int tbrParser( String @NotNull [] arr, int pos ) {
        String tbrStr = arr[pos];
        if ( QUALITY_PATTERN.matcher( tbrStr ).matches() ) {
            // Remove the letter 'k'
            tbrStr = tbrStr.substring( 0, tbrStr.length() - 1 );
            return Integer.parseInt( tbrStr );
        } else 
            throw new PatternMismatchException( tbrStr, "quality", arr );
    }

    static @NotNull BigInteger sizeParser( String @NotNull [] arr, int pos ) {
        String sizeStr = arr[pos];
        if ( !SIZE_PATTERN.matcher( sizeStr ).matches() )
            throw new PatternMismatchException( sizeStr, "file's size", arr );

        int pow = switch ( sizeStr.replaceAll( SIZE_UNIT, "" ) ) {
            case "KiB" -> 1;
            case "MiB" -> 2;
            case "GiB" -> 3;
            case "TiB" -> 4;
            case "PiB" -> 5;
            case "EiB" -> 6;
            case "ZiB" -> 7;
            case "YiB" -> 8;
            default -> 0;
        };

        String actualSize = sizeStr.replaceAll( SIZE_FORMAT, "" );
        double unconvertedSize = Double.parseDouble( actualSize );
        if ( pow != 0 ) 
            unconvertedSize *= Math.pow( 1024, pow );

        return BigInteger.valueOf( Math.round( unconvertedSize ) );
    }

    static int sampleRateParser( String @NotNull [] arr, int pos ) {
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