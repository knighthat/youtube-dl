package me.knighthat.extractor.youtube.response.format;

import java.math.BigInteger;

import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import com.google.gson.Gson;
import com.google.gson.JsonObject;

import me.knighthat.youtubedl.exception.InsufficientElementsException;
import me.knighthat.youtubedl.exception.PatternMismatchException;
import me.knighthat.youtubedl.response.formats.Format;

public class TestAudio {

    @NotNull
    private static final Gson GSON = new Gson();

    @NotNull
    static final String VALID_FORMAT = """
        {
            "format_id": "249",
            "url": "",
            "source_preference": -1,
            "quality": -1,
            "language": null,
            "language_preference": -1,
            "preference": null,
            "asr": 48000,
            "filesize": 1553825,
            "format_note": "audio_quality_low",
            "audio_channels": 2,
            "tbr": 51.733,
            "ext": "webm",
            "vcodec": "none",
            "acodec": "opus",
            "container": "webm_dash",
            "protocol": "http_dash_segments",
            "fragments": [
                {
                    "url": ""
                }
            ],
            "format": "249 - audio only (audio_quality_low)",
            "http_headers": {}
        }
    """;

    /* MISSING "asr" key */
    @NotNull
    static final String INVALID_FORMAT  = """
        {
            "format_id": "249",
            "url": "",
            "source_preference": -1,
            "quality": -1,
            "language": null,
            "language_preference": -1,
            "preference": null,
            "filesize": 1553825,
            "format_note": "audio_quality_low",
            "audio_channels": 2,
            "tbr": 51.733,
            "ext": "webm",
            "vcodec": "none",
            "acodec": "opus",
            "container": "webm_dash",
            "protocol": "http_dash_segments",
            "fragments": [
                {
                    "url": ""
                }
            ],
            "format": "249 - audio only (audio_quality_low)",
            "http_headers": {}
        }
    """;

    @Test
    void testValidJsonConstructor() {
        JsonObject validJson = GSON.fromJson( VALID_FORMAT, JsonObject.class );

        Assertions.assertDoesNotThrow( () -> new Audio( validJson ) );
        Audio audio = new Audio( validJson );
        Assertions.assertEquals( Format.Type.AUDIO_ONLY, audio.type() );
        Assertions.assertEquals( "249", audio.code() );
        Assertions.assertEquals( "webm", audio.extension() );
        Assertions.assertEquals( 52, audio.tbr() );
        Assertions.assertEquals( "opus", audio.aCodec() );
        Assertions.assertEquals( 48000, audio.sampleRate() );
        Assertions.assertEquals( BigInteger.valueOf( 1553825L ), audio.size() );
    }

    @Test
    void testInvalidJsonConstructor() {
        JsonObject invalidJson = GSON.fromJson( INVALID_FORMAT, JsonObject.class );

        Assertions.assertThrows( 
            NullPointerException.class, 
            () -> new Audio(invalidJson)
        );
    }

    @Test
    void testValidArrayConctructor() {
        @NotNull
        final String[] VALID_ARRAY = {
            "140", 
            "m4a", 
            "audio", 
            "only", 
            "audio_quality_medium", 
            "129k", 
            "m4a_dash container", 
            "mp4a.40.2 (44100Hz)", 
            "3.71MiB"
        };

        Assertions.assertDoesNotThrow( () -> new Audio( VALID_ARRAY ) );

        Audio audio = new Audio( VALID_ARRAY );
        Assertions.assertEquals( Format.Type.AUDIO_ONLY, audio.type() );
        Assertions.assertEquals( "140", audio.code() );
        Assertions.assertEquals( "m4a", audio.extension() );
        Assertions.assertEquals( 129, audio.tbr() );
        Assertions.assertEquals( "mp4a.40.2", audio.aCodec() );
        Assertions.assertEquals( 44100, audio.sampleRate() );
        Assertions.assertEquals( BigInteger.valueOf( 3890217L ), audio.size() );
    }

    @Test
    void testNotEnoughArgArrayConstructor() {
        @NotNull
        String[] NOT_ENOUGH_ARGUMENTS_ARRAY = {
            "140", 
            "m4a", 
            "audio", 
            "only", 
            "audio_quality_medium", 
            "m4a_dash container", 
            "mp4a.40.2 (44100Hz)", 
            "3.71MiB"
        };

        Assertions.assertThrows( 
            InsufficientElementsException.class,
            () -> new Audio( NOT_ENOUGH_ARGUMENTS_ARRAY )
        );
    }

    @Test
    void testWrongTypeArrayConstructor() {
        /* 129k -> 129b */
        @NotNull
        final String[] WRONG_BITRATE_FORMAT_ARRAY = {
            "140", 
            "m4a", 
            "audio", 
            "only", 
            "audio_quality_medium", 
            "129b", 
            "m4a_dash container", 
            "mp4a.40.2 (44100Hz)", 
            "3.71MiB"
        };
        Assertions.assertThrows( 
            PatternMismatchException.class, 
            () -> new Audio( WRONG_BITRATE_FORMAT_ARRAY )
        );

        /* 44100Hz -> 44100GHz */
        @NotNull
        final String[] WRONG_SAMPLE_RATE_FORMAT_ARRAY = {
            "140", 
            "m4a", 
            "audio", 
            "only", 
            "audio_quality_medium", 
            "129k", 
            "m4a_dash container", 
            "mp4a.40.2 (44100GHz)", 
            "3.71MiB"
        };
        Assertions.assertThrows(
            PatternMismatchException.class,
            () -> new Audio( WRONG_SAMPLE_RATE_FORMAT_ARRAY )
        );
    }
}