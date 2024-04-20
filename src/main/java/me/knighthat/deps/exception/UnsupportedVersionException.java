package me.knighthat.deps.exception;

import org.jetbrains.annotations.NotNull;

import java.io.Serial;

public class UnsupportedVersionException extends UnsupportedOperationException {

    @Serial
    private static final long serialVersionUID = 1857462938573627485L;

    public UnsupportedVersionException( @NotNull String name, @NotNull String required ) {
        super( "%s requires version %s or newer to run!".formatted( name, required ) );
    }
}
