package me.knighthat.youtubedl.response;

import org.jetbrains.annotations.NotNull;

import java.util.List;

/**
 * This response indicates that the returned data
 * from the requested server is a list.
 */
@FunctionalInterface
public interface ListResponse<T> extends Response {

    /**
     * Data from requested server that has been
     * converted into objects and packed into
     * a list.
     * 
     * @return a list of data returned from the server
     */
    @NotNull List<T> items();
}
