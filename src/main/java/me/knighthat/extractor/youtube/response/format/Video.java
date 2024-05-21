package me.knighthat.extractor.youtube.response.format;

import java.math.BigInteger;

import org.jetbrains.annotations.NotNull;

import com.google.gson.JsonObject;

import lombok.Getter;
import lombok.ToString;
import lombok.experimental.Accessors;
import me.knighthat.youtubedl.exception.InsufficientElementsException;
import me.knighthat.youtubedl.response.formats.Format;
import me.knighthat.youtubedl.response.formats.SizedMedia;
import me.knighthat.youtubedl.response.formats.VideoFormat;

/**
 * VideoOnly
 */
@Getter
@Accessors( fluent = true )
public class Video implements VideoFormat, SizedMedia {

    @NotNull
    @ToString.Exclude
    private final Format.Type type;
    @NotNull
    private final String code;
    @NotNull
    private final String extension;
    private final int tbr;
    @NotNull
    private final String vCodec;
    @NotNull
    private final String resolution;
    private final float fps;
    @NotNull
    private final BigInteger size;

    {
        this.type = me.knighthat.youtubedl.response.formats.Format.Type.VIDEO_ONLY;
    }

    public Video(String @NotNull [] arr) {
        /* [400, mp4, 2560x1440, 1440p, 5314k, mp4_dash container, av01.0.12M.08, 24fps, video only, 152.20MiB] */
        if ( arr.length < 10 )
            throw new InsufficientElementsException( "VideoOnly", arr.length, 10 );

        this.code = arr[0];
        this.extension = arr[1];
        this.tbr = FormatUtils.tbrParser( arr, 4 );
        this.fps = FormatUtils.parseFps(arr, 7);
        this.vCodec = arr[6];
        this.resolution = FormatUtils.parseResolution(arr, 3);
        this.size = FormatUtils.sizeParser( arr, arr.length - 1 );
    }

    public Video( @NotNull JsonObject json ) {
        /*
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
        */
        for ( String key : new String[] { "format_id", "ext", "tbr", "vcodec", "format_note", "fps", "filesize" } )
            if ( !json.has(key) )
                throw new NullPointerException(key + " does not exist!");

        this.code = json.get( "format_id" ).getAsString();
        this.extension = json.get( "ext" ).getAsString();
        float tbr = json.get( "tbr" ).getAsFloat();
        this.tbr = Math.round( tbr) ;
        this.vCodec = json.get( "vcodec" ).getAsString();
        this.resolution = json.get( "format_node" ).getAsString();
        this.fps = json.get( "fps" ).getAsFloat();
        this.size = FormatUtils.GSON.fromJson( json.get( "filesize" ), BigInteger.class );
    }
}