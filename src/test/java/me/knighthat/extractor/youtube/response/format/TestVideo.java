package me.knighthat.extractor.youtube.response.format;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import me.knighthat.youtubedl.exception.InsufficientElementsException;
import me.knighthat.youtubedl.exception.PatternMismatchException;
import me.knighthat.youtubedl.response.format.Format;
import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.math.BigInteger;

public class TestVideo {

    @NotNull
    private static final Gson GSON = new Gson();

    @NotNull
    static final String VALID_FORMAT = """
            {
                "format_id": "394",
                "url": "",
                "source_preference": -1,
                "quality": 0,
                "language": null,
                "language_preference": -1,
                "preference": null,
                "filesize": 1809891,
                "format_note": "144p",
                "fps": 24,
                "height": 144,
                "tbr": 60.269,
                "width": 256,
                "ext": "mp4",
                "vcodec": "av01.0.00M.08",
                "acodec": "none",
                "container": "mp4_dash",
                "protocol": "http_dash_segments",
                "fragments": [
                    {
                        "url": ""
                    }
                ],
                "format": "394 - 256x144 (144p)",
                "http_headers": {}
            }
        """;

    /* MISSING "fps" */
    @NotNull
    static final String INVALID_FORMAT = """
            {
                "format_id": "394",
                "url": "",
                "source_preference": -1,
                "quality": 0,
                "language": null,
                "language_preference": -1,
                "preference": null,
                "filesize": 1809891,
                "format_note": "144p",
                "height": 144,
                "tbr": 60.269,
                "width": 256,
                "ext": "mp4",
                "vcodec": "av01.0.00M.08",
                "acodec": "none",
                "container": "mp4_dash",
                "protocol": "http_dash_segments",
                "fragments": [
                    {
                        "url": ""
                    }
                ],
                "format": "394 - 256x144 (144p)",
                "http_headers": {}
            }
        """;

    @Test
    void testValidJsonConstructor() {
        JsonObject validJson = GSON.fromJson( VALID_FORMAT, JsonObject.class );
        Assertions.assertDoesNotThrow( () -> new Video( validJson ) );

        Video video = new Video( validJson );
        Assertions.assertEquals( Format.Type.VIDEO_ONLY, video.type() );
        Assertions.assertEquals( "394", video.code() );
        Assertions.assertEquals( "mp4", video.extension() );
        Assertions.assertEquals( 60, video.tbr() );
        Assertions.assertEquals( "av01.0.00M.08", video.vCodec() );
        Assertions.assertEquals( "144p", video.resolution() );
        Assertions.assertEquals( 24f, video.fps() );
        Assertions.assertEquals( BigInteger.valueOf( 1809891L ), video.size() );
    }

    @Test
    void testInvalidJsonConstructor() {
        JsonObject invalidJson = GSON.fromJson( INVALID_FORMAT, JsonObject.class );
        Assertions.assertThrows(
            NullPointerException.class,
            () -> new Video( invalidJson )
        );
    }

    @Test
    void testValidArrayConstructor() {
        @NotNull final String[] VALID_ARRAY = {
            "400",
            "mp4",
            "2560x1440",
            "1440p",
            "5314k",
            "mp4_dash container",
            "av01.0.12M.08",
            "24fps",
            "video only",
            "152.20MiB"
        };
        Assertions.assertDoesNotThrow( () -> new Video( VALID_ARRAY ) );

        Video video = new Video( VALID_ARRAY );
        Assertions.assertEquals( Format.Type.VIDEO_ONLY, video.type() );
        Assertions.assertEquals( "400", video.code() );
        Assertions.assertEquals( "mp4", video.extension() );
        Assertions.assertEquals( 5314, video.tbr() );
        Assertions.assertEquals( "av01.0.12M.08", video.vCodec() );
        Assertions.assertEquals( "1440p", video.resolution() );
        Assertions.assertEquals( 24f, video.fps() );
        Assertions.assertEquals( BigInteger.valueOf( 159593267L ), video.size() );
    }

    @Test
    void testNotEnoughArgArrayConstructor() {
        /* MISSING "filesize" */
        @NotNull
        String[] NOT_ENOUGH_ARGUMENTS_ARRAY = {
            "400",
            "mp4",
            "2560x1440",
            "1440p",
            "5314k",
            "mp4_dash container",
            "av01.0.12M.08",
            "24fps",
            "video only",
            };

        Assertions.assertThrows(
            InsufficientElementsException.class,
            () -> new Video( NOT_ENOUGH_ARGUMENTS_ARRAY )
        );
    }

    @Test
    void testWrongTypeArrayConstructor() {
        /* 5314k -> 5314b */
        @NotNull final String[] WRONG_BITRATE_FORMAT_ARRAY = {
            "400",
            "mp4",
            "2560x1440",
            "1440p",
            "5314b",
            "mp4_dash container",
            "av01.0.12M.08",
            "24fps",
            "video only",
            "152.20MiB"
        };
        Assertions.assertThrows(
            PatternMismatchException.class,
            () -> new Video( WRONG_BITRATE_FORMAT_ARRAY )
        );

        /* 24fps -> 24 */
        @NotNull final String[] WRONG_FPS_FORMAT_ARRAY = {
            "400",
            "mp4",
            "2560x1440",
            "1440p",
            "5314k",
            "mp4_dash container",
            "av01.0.12M.08",
            "24",
            "video only",
            "152.20MiB"
        };
        Assertions.assertThrows(
            PatternMismatchException.class,
            () -> new Video( WRONG_FPS_FORMAT_ARRAY )
        );
    }
}