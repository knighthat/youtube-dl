package me.knighthat.extractor.youtube;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.fail;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import me.knighthat.extractor.youtube.response.format.Video;
import me.knighthat.extractor.youtube.response.format.Audio;
import me.knighthat.extractor.youtube.response.format.Mix;
import me.knighthat.youtubedl.YoutubeDL;
import me.knighthat.youtubedl.exception.UnsupportedVersionException;
import me.knighthat.youtubedl.response.format.Format;
import me.knighthat.youtubedl.response.subtitle.Subtitle;
import me.knighthat.youtubedl.response.thumbnail.Thumbnail;

/*
 * This class tests "formats", "thumbnails", "subtitles", and "video"
 * methods of {@link me.knighthat.extractor.youtube.YouTube} class.
 * 
 * "stream" and "json" are excluded because the methods use default
 * retrieve methods instead of making their own.
 */
public class TestYouTube {

    @NotNull
    private static final String URL = "https://www.youtube.com/watch?v=Xs0Exw7O7B0";

    @BeforeAll
    static void setUp() {
        try {
            YoutubeDL.init();
        } catch (UnsupportedVersionException | IOException e) {
            fail( "failed to setup python/youtube-dl!", e);
        }
    }

    @Test
    void testFormatsExtractor() {
        List<Format> formats = YouTube.formats( URL ).execute().items();

        for( Format format : formats ) {
            if ( 
                format instanceof Audio
                || format instanceof Video
                || format instanceof Mix
            )
                continue;
            else
                fail(format.getClass().getName() + " is not a predefined YouTube's format!");
        }
    }

    @Test
    void testThumbnailsExtractor() {
        List<Thumbnail> thumbnails = YouTube.thumbnails( URL ).execute().items();

        for( Thumbnail thumbnail : thumbnails )
            assertInstanceOf(
                me.knighthat.extractor.youtube.response.thumbnail.Thumbnail.class, 
                thumbnail
            );
    }

    @Test
    void testSubtitlesExtractor() {
        List<Subtitle> subtitles = YouTube.subtitles( URL ).execute().items();

        for( Subtitle subtitle : subtitles )
            assertInstanceOf(
                me.knighthat.extractor.youtube.response.subtitle.Subtitle.class, 
                subtitle 
            );
    }

    @Test
    void testVideoExtractor() {
        Optional<me.knighthat.youtubedl.response.video.Video> video = YouTube.video( URL ).execute().result();

        assertDoesNotThrow( () -> video.get() );
        Assertions.assertInstanceOf(
            me.knighthat.extractor.youtube.response.Video.class, 
            video.get()
        );
    }
}