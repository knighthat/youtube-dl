package me.knighthat.extractor.youtube.response.format;

import java.math.BigInteger;

import org.jetbrains.annotations.NotNull;

import com.google.gson.JsonObject;

import lombok.Getter;
import lombok.ToString;
import lombok.experimental.Accessors;
import me.knighthat.youtubedl.exception.InsufficientElementsException;
import me.knighthat.youtubedl.response.formats.AudioFormat;
import me.knighthat.youtubedl.response.formats.Format;
import me.knighthat.youtubedl.response.formats.SizedMedia;

/**
 * Audio
 */
@Getter
@Accessors( fluent = true )
public class Audio implements AudioFormat, SizedMedia {

    @NotNull
    @ToString.Exclude
    private final Format.Type type;
    @NotNull
    private final String code;
    @NotNull
    private final String extension;
    private final int tbr;
    @NotNull
    private final String aCodec;
    private final int sampleRate;
    @NotNull
    private final BigInteger size;

    {
        this.type = me.knighthat.youtubedl.response.formats.Format.Type.AUDIO_ONLY;
    }

    public Audio ( String @NotNull [] arr ) {
        /* [140, m4a, audio, only, audio_quality_medium, 129k, m4a_dash container, mp4a.40.2 (44100Hz), 3.71MiB] */
        if ( arr.length < 9 )
            throw new InsufficientElementsException( "AudioOnly", arr.length, 9 );

        this.code = arr[0];
        this.extension = arr[1];
        this.tbr = FormatUtils.tbrParser( arr, 5 );
        this.aCodec = arr[7].split( "\\s" )[0];
        this.sampleRate = FormatUtils.sampleRateParser( arr, 7 );
        this.size = FormatUtils.sizeParser( arr, arr.length - 1 );
    }
    
    public Audio ( @NotNull JsonObject json ) {
        /*
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
        */
        for ( String key : new String[] { "format_id", "ext", "tbr", "acodec", "asr", "filesize" } )
            if ( !json.has(key) )
                throw new NullPointerException(key + " does not exist!");   

        this.code = json.get( "format_id" ).getAsString();
        this.extension = json.get( "ext" ).getAsString();
        float tbr = json.get( "tbr" ).getAsFloat();
        this.tbr = Math.round( tbr) ;
        this.aCodec = json.get( "acodec" ).getAsString();
        this.sampleRate = json.get( "asr" ).getAsInt();
        this.size = FormatUtils.GSON.fromJson( json.get( "filesize" ), BigInteger.class );
    }
}