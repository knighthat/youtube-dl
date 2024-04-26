package me.knighthat.youtubedl.response.format;

import lombok.Getter;
import lombok.experimental.Accessors;
import me.knighthat.youtubedl.exception.PatternMismatchException;
import org.jetbrains.annotations.NotNull;

import java.util.regex.Pattern;

public class SizedMedia extends Format {

    @NotNull
    static final Pattern SIZE_PATTERN = Pattern.compile( "^\\d+(\\.\\d+)?[KMGTPEZY]?iB$" );

    @Getter
    @Accessors( chain = true, fluent = true )
    private final float size;

    protected SizedMedia( @NotNull Type type, String @NotNull [] arr ) throws PatternMismatchException {
        super( type, arr );

        String sizeStr = arr[arr.length - 1];
        if ( !SIZE_PATTERN.matcher( sizeStr ).matches() )
            throw new PatternMismatchException( sizeStr, "file's size", arr );

        sizeStr = sizeStr.substring( 0, sizeStr.length() - 3 );
        this.size = Float.parseFloat( sizeStr );
    }

    protected SizedMedia( @NotNull Type type, int code, @NotNull String extension, int kbps, float size ) {
        super( type, code, extension, kbps );
        this.size = size;
    }
}
