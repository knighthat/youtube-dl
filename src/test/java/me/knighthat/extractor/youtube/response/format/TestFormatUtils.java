package me.knighthat.extractor.youtube.response.format;

import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import me.knighthat.youtubedl.exception.PatternMismatchException;

public class TestFormatUtils {

    @Test
    void testFpsParser() {
        @NotNull
        final String[] SAMPLES = { 
            "24fps", 
            "13.4fps", 
            "123.123.123fps", 
            "abcfps", 
            "123abc" 
        };

        /* Valid */
        Assertions.assertDoesNotThrow( 
            () -> FormatUtils.fpsParser( SAMPLES, 0 )
        );
        Assertions.assertDoesNotThrow(
            () -> FormatUtils.fpsParser( SAMPLES, 1 )
        );
        
        /* Invalid format */
        Assertions.assertThrows(
            PatternMismatchException.class,
            () -> FormatUtils.fpsParser( SAMPLES, 2 )
        );
        Assertions.assertThrows(
            PatternMismatchException.class,
            () -> FormatUtils.fpsParser( SAMPLES, 3 )
        );
        Assertions.assertThrows(
            PatternMismatchException.class, 
            () -> FormatUtils.fpsParser( SAMPLES, 4 )
        );
    }

    @Test
    void testResolutionParser() {
        @NotNull
        final String[] SAMPLES = {
            "1440p",
            "123c",
            "abcp"
        };

        /* Valid */
        Assertions.assertDoesNotThrow(
            () -> FormatUtils.reolutionParser( SAMPLES, 0 )
        );

        /* Invalid format */
        Assertions.assertThrows(
            PatternMismatchException.class, 
            () -> FormatUtils.reolutionParser( SAMPLES, 1 )
        );
        Assertions.assertThrows(
            PatternMismatchException.class, 
            () -> FormatUtils.reolutionParser( SAMPLES, 2 )
        );
    }

    @Test
    void testTbrParser() {
        @NotNull
        final String[] SAMPLES = {
            "123k",
            "123b",
            "abck"
        };

        /* Valid */
        Assertions.assertDoesNotThrow(
            () -> FormatUtils.tbrParser( SAMPLES, 0 )
        );

        /* Invalid format */
        Assertions.assertThrows(
            PatternMismatchException.class,
            () -> FormatUtils.tbrParser( SAMPLES, 1 )
        );
        Assertions.assertThrows(
            PatternMismatchException.class,
            () -> FormatUtils.tbrParser( SAMPLES, 2 )
        );
    }

    @Test
    void testSizeParser() {
        @NotNull
        final String[] SAMPLES = {
            "1234KiB",
            "1MB",
            "abcMiB"
        };

        /* Valid */
        Assertions.assertDoesNotThrow(
            () -> FormatUtils.sizeParser( SAMPLES, 0 )
        );

        /* Invalid format */
        Assertions.assertThrows(
            PatternMismatchException.class, 
            () -> FormatUtils.sizeParser( SAMPLES, 1 )
        );
        Assertions.assertThrows(
            PatternMismatchException.class, 
            () -> FormatUtils.sizeParser( SAMPLES, 2 )
        );
    }

    @Test
    void testSampleRateParser() {
        @NotNull
        final String[] SAMPLES = {
            "44100Hz",
            "opus  (48000Hz)",
            "mp4a.40.2 (44100Hz)",
            "abcHz",
            "44100GHz"
        };

        /* Valid */
        Assertions.assertDoesNotThrow(
            () -> FormatUtils.sampleRateParser( SAMPLES, 0 )
        );
        Assertions.assertDoesNotThrow(
            () -> FormatUtils.sampleRateParser( SAMPLES, 1 )
        );
        Assertions.assertDoesNotThrow(
            () -> FormatUtils.sampleRateParser( SAMPLES, 2 )
        );

        /* Invalid format */
        Assertions.assertThrows(
            PatternMismatchException.class, 
            () -> FormatUtils.sampleRateParser( SAMPLES, 3 )
        );
        Assertions.assertThrows(
            PatternMismatchException.class, 
            () -> FormatUtils.sampleRateParser( SAMPLES, 4 )
        );
    }
}