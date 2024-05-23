package me.knighthat.extractor.youtube.response.format;

import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import com.google.gson.Gson;
import com.google.gson.JsonObject;

import me.knighthat.youtubedl.exception.InsufficientElementsException;
import me.knighthat.youtubedl.exception.PatternMismatchException;
import me.knighthat.youtubedl.response.format.Format;

public class TestMix {

    @NotNull
    private static final Gson GSON = new Gson();

    @NotNull
    static String VALID_FORMAT = """
        {
            "format_id": "18",
            "url": "",
            "source_preference": -1,
            "quality": 2,
            "language": null,
            "language_preference": -1,
            "preference": null,
            "asr": 44100,
            "format_note": "360p",
            "fps": 24,
            "audio_channels": 2,
            "height": 360,
            "tbr": 605.181,
            "width": 640,
            "ext": "mp4",
            "vcodec": "avc1.42001E",
            "acodec": "mp4a.40.2",
            "format": "18 - 640x360 (360p)",
            "protocol": "https",
            "http_headers": {}
        }
    """;

    /* MISSING "tbr" */
    @NotNull
    static String INVALID_FORMAT = """
        {
            "format_id": "18",
            "url": "",
            "source_preference": -1,
            "quality": 2,
            "language": null,
            "language_preference": -1,
            "preference": null,
            "asr": 44100,
            "format_note": "360p",
            "fps": 24,
            "audio_channels": 2,
            "height": 360,
            "width": 640,
            "ext": "mp4",
            "vcodec": "avc1.42001E",
            "acodec": "mp4a.40.2",
            "format": "18 - 640x360 (360p)",
            "protocol": "https",
            "http_headers": {}
        }
    """;

    @Test
    void testValidJsonConstructor() {
        JsonObject validJson = GSON.fromJson( VALID_FORMAT, JsonObject.class );
        Assertions.assertDoesNotThrow( () -> new Mix( validJson ) );

        Mix mix = new Mix( validJson );
        Assertions.assertEquals( Format.Type.MIXED, mix.type() );
        Assertions.assertEquals( "18", mix.code() );
        Assertions.assertEquals( "mp4", mix.extension() );
        Assertions.assertEquals( 605, mix.tbr() );
        Assertions.assertEquals( "avc1.42001E", mix.vCodec() );
        Assertions.assertEquals( "mp4a.40.2", mix.aCodec() );
        Assertions.assertEquals( 44100, mix.sampleRate() );
        Assertions.assertEquals( "360p", mix.resolution() );
        Assertions.assertEquals( 24f, mix.fps() );
    }

    @Test
    void testInvalidJsonConstructor() {
        JsonObject invalidJson = GSON.fromJson( INVALID_FORMAT, JsonObject.class );
        Assertions.assertThrows( 
            NullPointerException.class, 
            () -> new Mix(invalidJson)
        );
    }

    @Test
    void testValidArrayConctructor() {
        @NotNull
        final String[] VALID_ARRAY = { 
            "18", 
            "mp4", 
            "640x360", 
            "360p", 
            "595k", 
            "avc1.42001E", 
            "24fps", 
            "mp4a.40.2 (44100Hz) (best)"
        };
        Assertions.assertDoesNotThrow( () -> new Mix( VALID_ARRAY ));

        Mix mix = new Mix( VALID_ARRAY );
        Assertions.assertEquals( Format.Type.MIXED, mix.type() );
        Assertions.assertEquals( "18", mix.code() );
        Assertions.assertEquals( "mp4", mix.extension() );
        Assertions.assertEquals( 595, mix.tbr() );
        Assertions.assertEquals( "avc1.42001E", mix.vCodec() );
        Assertions.assertEquals( "mp4a.40.2", mix.aCodec() );
        Assertions.assertEquals( 44100, mix.sampleRate() );
        Assertions.assertEquals( "360p", mix.resolution() );
        Assertions.assertEquals( 24f, mix.fps() );
    }

    @Test
    void testNotEnoughArgArrayConstructor() {
        /* MISSING "vcodec" */
        @NotNull
        String[] NOT_ENOUGH_ARGUMENTS_ARRAY = { 
            "18", 
            "mp4", 
            "640x360", 
            "360p", 
            "595k", 
            "24fps", 
            "mp4a.40.2 (44100Hz) (best)"
        };

        Assertions.assertThrows( 
            InsufficientElementsException.class,
            () -> new Mix( NOT_ENOUGH_ARGUMENTS_ARRAY )
        );
    }

    @Test
    void testWrongTypeArrayConstructor() {
        /* 595k -> 595b */
        @NotNull
        final String[] WRONG_BITRATE_FORMAT_ARRAY = { 
            "18", 
            "mp4", 
            "640x360", 
            "360p", 
            "595b", 
            "avc1.42001E", 
            "24fps", 
            "mp4a.40.2 (44100Hz) (best)"
        };
        Assertions.assertThrows( 
            PatternMismatchException.class, 
            () -> new Mix( WRONG_BITRATE_FORMAT_ARRAY )
        );

        /* 44100Hz -> 44100GHz */
        @NotNull
        final String[] WRONG_SAMPLE_RATE_FORMAT_ARRAY = { 
            "18", 
            "mp4", 
            "640x360", 
            "360p", 
            "595k", 
            "avc1.42001E", 
            "24fps", 
            "mp4a.40.2 (44100GHz) (best)"
        };
        Assertions.assertThrows(
            PatternMismatchException.class,
            () -> new Mix( WRONG_SAMPLE_RATE_FORMAT_ARRAY )
        );
    }
}