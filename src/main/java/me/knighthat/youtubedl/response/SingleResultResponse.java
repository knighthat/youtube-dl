package me.knighthat.youtubedl.response;

import org.jetbrains.annotations.NotNull;

/**
 * This response indicates that the returned data
 * from the requested server is a non-null object.
 */
@FunctionalInterface
public interface SingleResultResponse<T> extends Response {

    /**
     * Object parsed from the server must always be
     * a non-null data
     * 
     * Object parsed from server can be null, therefore,
     * it is packed inside {@link java.util.Optional}.
     * This is a safety measure to ensure that
     * user know about the possible null value,
     * so they can take proper action to prevent
     * the program from unexpected crashes.
     * 
     * @return Data from requested server that has been converted into an object.
     */
    @NotNull T result();
}
