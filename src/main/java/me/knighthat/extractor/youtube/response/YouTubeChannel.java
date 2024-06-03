package me.knighthat.extractor.youtube.response;

import org.jetbrains.annotations.NotNull;

/**
 * An interface represents basic information of a YouTube channel
 */
public interface YouTubeChannel {

    /**
     * Id of a channel remains even when
     * the handle* has been changed by the owner
     * 
     * @return channel's persistent id.
     */
    @NotNull String id();

    /**
     * YouTube channels are allowed to have unique
     * strings that give viewer a better way to
     * find their channels.
     * <p>
     * A channel's handle is not persistent and 
     * can be changed by the owner.
     * To ensure the persistence, use {@link me.knighthat.youtubedl.YouTubeChannel.youtube.response.Channel#id()}.
     * 
     * @return unique and readable string. Always starts with '@'
     */
    @NotNull String handle();

    /**
     * Reflects the channel’s brand, tone, and personality, 
     * which can help in building a consistent and recognizable brand identity.
     * 
     * @return name of the channel
     */
    @NotNull String title();

    /**
     * Newer version of channel's URL. It uses the current
     * handle to create a link leads directly to the uploader
     * <p>
     * https://youtube.com/@example
     * 
     * @return channel's URL constructed using channel's handler
     */
    @NotNull String uploaderUrl();

    /**
     * Legacy channel's URL
     * <p>
     * https://youtube.com/channel/exmapleId
     * 
     * @return legacy youtube channel's URL
     */
    @NotNull String channelUrl();
}