package me.knighthat.youtubedl.response;

import org.jetbrains.annotations.NotNull;

import java.util.function.BiConsumer;

/**
 * This response allows caller to access
 * the data stream being pulled from the server
 * in real-time.
 */
@FunctionalInterface
public interface RealtimeResponse extends Response {

    /**
     * Some data can be accessed as it 
     * is downloaded from the server.
     * <p>
     * It takes two parameters. 
     * The first being the size of each chunk
     * being read (download) from the server.
     * The second is the way to handle the stream.
     * <p>
     * Second param is a {@link java.util.function.BiConsumer}
     * that returns a byte array represents the data
     * read from the data stream.
     * Then an integer represents the size of the actual bytes read.
     * <p>
     * Byte array from second param has the fixed size of the first param.
     * But the data pulled from data stream is not always filled the array
     * (especially at the end of the stream). 
     * Therefore, an integer is there to tell the real chunk's size.
     * 
     * @param bufferSize how big each chunk is being read
     * @param stream how to handle each chunk of data
     */
    void stream( int bufferSize, @NotNull BiConsumer<byte[], Integer> stream );
}
