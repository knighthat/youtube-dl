package me.knighthat.internal.utils;

import me.knighthat.youtubedl.exception.PatternMismatchException;
import org.jetbrains.annotations.NotNull;

import java.math.BigInteger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * FormatUtils
 */
public final class FormatUtils {

    @NotNull
    private static final String  SIZE_FORMAT           = "^\\d+(\\.\\d+)?";
    @NotNull
    private static final String  SIZE_UNIT             = "[KMGTPEZY]?iB$";
    @NotNull
    private static final Pattern FPS_PATTERN           = Pattern.compile( "^\\d+(\\.\\d+)? *fps$" );
    @NotNull
    private static final Pattern RESOLUTION_PATTERN    = Pattern.compile( "^\\d+p$" );
    @NotNull
    private static final Pattern QUALITY_PATTERN       = Pattern.compile( "^\\d+k$" );
    @NotNull
    private static final Pattern SIZE_PATTERN          = Pattern.compile( SIZE_FORMAT + SIZE_UNIT );
    @NotNull
    private static final Pattern SAMPLING_RATE_PATTERN = Pattern.compile( "\\d+ *Hz" );
    @NotNull
    private static final Pattern DIMENSION_PATTERN     = Pattern.compile( "^\\d{3,4}x\\d{3,4}$" );

    public static float fpsParser( String @NotNull [] arr, int pos ) throws PatternMismatchException {
        String fpsStr = arr[pos].trim();
        if ( FPS_PATTERN.matcher( fpsStr ).matches() ) {
            String framePerSecond = fpsStr.replace( "fps", "" );
            return Float.parseFloat( framePerSecond );
        } else
            throw new PatternMismatchException( fpsStr, "fps", arr );
    }

    public static @NotNull String resolutionParser( String @NotNull [] arr, int pos ) throws PatternMismatchException {
        String result = arr[pos].trim();

        if ( DIMENSION_PATTERN.matcher( result ).matches() )
            result = result.split( "x" )[1];
        else if ( !RESOLUTION_PATTERN.matcher( result ).matches() )
            throw new PatternMismatchException( result, "resolution", arr );

        return result;
    }

    public static int tbrParser( String @NotNull [] arr, int pos ) {
        String tbrStr = arr[pos].trim();
        if ( QUALITY_PATTERN.matcher( tbrStr ).matches() ) {
            // Remove the letter 'k'
            tbrStr = tbrStr.substring( 0, tbrStr.length() - 1 );
            return Integer.parseInt( tbrStr );
        } else
            throw new PatternMismatchException( tbrStr, "quality", arr );
    }

    public static @NotNull BigInteger sizeParser( String @NotNull [] arr, int pos ) {
        String sizeStr = arr[pos].trim();
        if ( !SIZE_PATTERN.matcher( sizeStr ).matches() )
            throw new PatternMismatchException( sizeStr, "file's size", arr );

        int pow = switch (sizeStr.replaceAll( SIZE_FORMAT, "" )) {
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

        String actualSize = sizeStr.replaceAll( SIZE_UNIT, "" );
        double unconvertedSize = Double.parseDouble( actualSize );
        if ( pow != 0 )
            unconvertedSize *= Math.pow( 1024, pow );

        return BigInteger.valueOf( Math.round( unconvertedSize ) );
    }

    public static int sampleRateParser( String @NotNull [] arr, int pos ) {
        String smplStr = arr[pos].trim();
        Matcher matcher = SAMPLING_RATE_PATTERN.matcher( smplStr );
        if ( !matcher.find() )
            throw new PatternMismatchException( smplStr, "sampling rate", arr );

        smplStr = smplStr.substring( matcher.start(), matcher.end() - 2 );
        return Integer.parseInt( smplStr );
    }

    public static String @NotNull [] split( @NotNull String str ) {
        String[] parts = str.trim().split( "," );
        String[] info = parts[0].trim().split( "\\s+" );

        String[] all = new String[parts.length + info.length - 1];
        System.arraycopy( info, 0, all, 0, info.length );
        System.arraycopy( parts, 1, all, info.length, parts.length - 1 );

        return all;
    }
}