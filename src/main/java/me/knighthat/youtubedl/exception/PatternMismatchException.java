package me.knighthat.youtubedl.exception;

import org.jetbrains.annotations.NotNull;

import java.io.Serial;
import java.util.Arrays;

public class PatternMismatchException extends IllegalArgumentException {

    @Serial
    private static final long serialVersionUID = -2837465938475628394L;

    public PatternMismatchException( @NotNull String value, @NotNull String patternName, String @NotNull [] arr ) {
        super( "%s does not follow %s pre-defined pattern %s".formatted( value, patternName, Arrays.toString( arr ) ) );
    }
}
