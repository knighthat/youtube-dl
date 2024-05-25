package me.knighthat.extractor.youtube;

import org.jetbrains.annotations.NotNull;

import me.knighthat.extractor.youtube.command.*;
import me.knighthat.youtubedl.command.Json;
import me.knighthat.youtubedl.command.Stream;

/**
 * YouTube extractor
 */
public class YouTube {

    public static @NotNull Formats.Builder formats( @NotNull String url ) { return Formats.builder( url ); }

    public static @NotNull Thumbnails.Builder thumbnails( @NotNull String url ) { return Thumbnails.builder( url ); }

    public static @NotNull Stream.Builder stream( @NotNull String url ) { return Stream.builder( url ); }

    public static @NotNull Subtitles.Builder subtitles( @NotNull String url ) { return Subtitles.builder( url ); }

    public static @NotNull Json.Builder json( @NotNull String url ) { return Json.builder( url ); }

    public static @NotNull Video.Builder video( @NotNull String url ) { return Video.builder( url ); }
}