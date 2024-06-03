package me.knighthat.extractor.youtube;

import org.jetbrains.annotations.NotNull;

import me.knighthat.extractor.youtube.command.Subtitles;
import me.knighthat.extractor.youtube.command.SubtitlesImpl;
import me.knighthat.extractor.youtube.command.Thumbnails;
import me.knighthat.extractor.youtube.command.ThumbnailsImpl;
import me.knighthat.extractor.youtube.command.VideoImpl;
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
}