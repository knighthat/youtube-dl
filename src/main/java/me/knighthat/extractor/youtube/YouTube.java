package me.knighthat.extractor.youtube;

import java.math.BigInteger;
import java.util.Date;
import java.util.Set;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Unmodifiable;

import me.knighthat.extractor.youtube.command.Subtitles;
import me.knighthat.extractor.youtube.command.SubtitlesImpl;
import me.knighthat.extractor.youtube.command.Thumbnails;
import me.knighthat.extractor.youtube.command.ThumbnailsImpl;
import me.knighthat.extractor.youtube.command.VideoImpl;
import me.knighthat.internal.annotation.Second;
import me.knighthat.youtubedl.command.Formats;
import me.knighthat.youtubedl.command.Json;
import me.knighthat.youtubedl.command.JsonImpl;
import me.knighthat.youtubedl.command.Stream;
import me.knighthat.youtubedl.command.StreamImpl;
import me.knighthat.youtubedl.response.format.AudioFormat;
import me.knighthat.youtubedl.response.format.MixFormat;
import me.knighthat.youtubedl.response.format.SizedMedia;
import me.knighthat.youtubedl.response.format.VideoFormat;


/**
 * YouTube extractor
 */
public class YouTube {

    public static @NotNull Formats.Builder formats( @NotNull String url ) { 
        return me.knighthat.extractor.youtube.command.Formats.builder( url ); 
    }

    public static @NotNull Thumbnails.Builder thumbnails( @NotNull String url ) { 
        return ThumbnailsImpl.builder( url ); 
    }

    public static @NotNull Stream.Builder stream( @NotNull String url ) { 
        return StreamImpl.builder( url ); 
    }

    public static @NotNull Subtitles.Builder subtitles( @NotNull String url ) { 
        return SubtitlesImpl.builder( url ); 
    }

    public static @NotNull Json.Builder json( @NotNull String url ) { 
        return JsonImpl.builder( url ); 
    }

    public static @NotNull me.knighthat.extractor.youtube.command.Video.Builder video( @NotNull String url ) { 
        return VideoImpl.builder( url ); 
    }

    public static interface Format {
    
        public static interface Video extends VideoFormat, SizedMedia {
        }

        public static interface Audio extends AudioFormat, SizedMedia {
        }

        public static interface Mix extends MixFormat {
        }
    }

    /**
     * Contains more details about a thumbnail, such as width and height.
     */
    public static interface Thumbnail extends me.knighthat.youtubedl.response.thumbnail.Thumbnail {
    
        int width();

        int height();
    }

    /**
     * A class represents youtube's subtitle.
     */
    public static interface Subtitle extends me.knighthat.youtubedl.response.subtitle.Subtitle {
    
        boolean isAutomatic();
    }

    /**
     * DownloadableSubtitle
     */
    public static interface DownloadableSubtitle extends me.knighthat.youtubedl.response.subtitle.DownloadableSubtitle {
    
        boolean isAutomatic();
    }

    /**
     * An interface represents basic information of a YouTube channel
     */
    public static interface Channel {
    
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

    /**
     * Detail information of a YouTube video.
     */
    public static interface Video extends me.knighthat.youtubedl.response.video.Video {
    
        @NotNull String description();

        @NotNull Date uploadDate();

        @Second long duration();
        
        @NotNull BigInteger views();

        @NotNull BigInteger likes();
        
        @NotNull Channel uploader();

        @NotNull @Unmodifiable Set<Thumbnail> thumbnails();

        @NotNull @Unmodifiable Set<DownloadableSubtitle> subtitles();
    }
}