package me.knighthat.extractor.youtube.command;

/**
 * Subtitles
 */
public class Subtitles extends{

    @NotNull
    private static final Pattern AUTOMATIC_SUBTITLE_PATTERN = Pattern.compile( "Available automatic captions for \\w+:" );
}