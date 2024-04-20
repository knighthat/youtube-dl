package me.knighthat.deps.exception;

import org.jetbrains.annotations.NotNull;

import java.io.Serial;

public class InsufficientElementsException extends IllegalArgumentException {

    @Serial
    private static final long serialVersionUID = -2857362958472647385L;

    public InsufficientElementsException( @NotNull String name, int provided, int required ) {
        super( "%s required at least %d elements to proceed (got %d)!".formatted( name, required, provided ) );
    }
}
