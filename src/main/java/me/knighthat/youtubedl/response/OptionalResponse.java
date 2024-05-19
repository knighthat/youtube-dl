package me.knighthat.youtubedl.response;

import org.jetbrains.annotations.NotNull;

import java.util.Optional;

/**
 * This response indicates that the returned data
 * from requested server is an object that may
 * be null.
 */
@FunctionalInterface
public interface OptionalResponse<T> extends SingleResultResponse<Optional<T>> {

    /**
     * Object parsed from the server can be null, 
     * therefore, it is packed inside {@link java.util.Optional}.
     * This is a safety measure to ensure that
     * user know about the possible null value,
     * so they can take proper action to prevent
     * the program from unexpected crashes.
     * 
     * @return Data from requested server that has been converted into an object.
     */
    @NotNull Optional<T> result();
}
