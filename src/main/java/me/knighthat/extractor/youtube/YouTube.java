package me.knighthat.extractor.youtube;

import org.jetbrains.annotations.NotNull;

import me.knighthat.extractor.youtube.command.Video;
import me.knighthat.extractor.youtube.command.VideoImpl;
import me.knighthat.youtubedl.command.Formats;
import me.knighthat.youtubedl.command.Json;
import me.knighthat.youtubedl.command.JsonImpl;
import me.knighthat.youtubedl.command.Stream;
import me.knighthat.youtubedl.command.StreamImpl;
import me.knighthat.youtubedl.command.Subtitles;
import me.knighthat.youtubedl.command.Thumbnails;


/**
 * YouTube extractor
 */
public class YouTube {

    public static @NotNull Formats.Builder formats( @NotNull String url ) { 
        return me.knighthat.extractor.youtube.command.Formats.builder( url ); 
    }

    public static @NotNull Thumbnails.Builder thumbnails( @NotNull String url ) { 
        return me.knighthat.extractor.youtube.command.Thumbnails.builder( url ); 
    }

    public static @NotNull Stream.Builder stream( @NotNull String url ) { 
        return StreamImpl.builder( url ); 
    }

    public static @NotNull Subtitles.Builder subtitles( @NotNull String url ) { 
        return me.knighthat.extractor.youtube.command.Subtitles.builder( url ); 
    }

    public static @NotNull Json.Builder json( @NotNull String url ) { 
        return JsonImpl.builder( url ); 
    }

    public static @NotNull Video.Builder video( @NotNull String url ) { 
        return VideoImpl.builder( url ); 
    }
}