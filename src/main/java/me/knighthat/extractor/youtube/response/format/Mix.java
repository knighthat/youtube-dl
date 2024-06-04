package me.knighthat.extractor.youtube.response.format;

import com.google.gson.JsonObject;
import lombok.Getter;
import lombok.ToString;
import lombok.experimental.Accessors;
import me.knighthat.extractor.youtube.YouTube;
import me.knighthat.internal.utils.FormatUtils;
import me.knighthat.youtubedl.exception.InsufficientElementsException;
import me.knighthat.youtubedl.response.format.Format;
import org.jetbrains.annotations.NotNull;

/**
 * Mix
 */
@Getter
@Accessors( fluent = true )
public class Mix implements YouTube.Format.Mix {

    @NotNull
    @ToString.Exclude
    private final Format.Type type;
    @NotNull
    private final String      code;
    @NotNull
    private final String      extension;
    private final int         tbr;
    @NotNull
    private final String      vCodec;
    @NotNull
    private final String      aCodec;
    private final int         sampleRate;
    @NotNull
    private final String      resolution;
    private final float       fps;

    {
        this.type = me.knighthat.youtubedl.response.format.Format.Type.MIXED;
    }

    public Mix( String @NotNull [] arr ) {
        /* [18, mp4, 640x360, 360p, 595k, avc1.42001E, 24fps, mp4a.40.2 (44100Hz) (best)] */
        if ( arr.length < 8 )
            throw new InsufficientElementsException( "Mix", arr.length, 8 );

        this.code = arr[0];
        this.extension = arr[1];
        this.tbr = FormatUtils.tbrParser( arr, 4 );
        this.vCodec = arr[5];
        this.aCodec = arr[7].split( "\\s+" )[0];
        this.sampleRate = FormatUtils.sampleRateParser( arr, 7 );
        this.resolution = FormatUtils.resolutionParser( arr, 3 );
        this.fps = FormatUtils.fpsParser( arr, 6 );
    }

    public Mix( @NotNull JsonObject json ) {
        /*
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
        */
        for (String key : new String[]{ "format_id", "ext", "tbr", "vcodec", "acodec", "asr", "format_note", "fps" })
            if ( !json.has( key ) )
                throw new NullPointerException( key + " does not exist!" );

        this.code = json.get( "format_id" ).getAsString();
        this.extension = json.get( "ext" ).getAsString();
        float tbr = json.get( "tbr" ).getAsFloat();
        this.tbr = Math.round( tbr );
        this.vCodec = json.get( "vcodec" ).getAsString();
        this.aCodec = json.get( "acodec" ).getAsString();
        this.sampleRate = json.get( "asr" ).getAsInt();
        this.resolution = json.get( "format_note" ).getAsString();
        this.fps = json.get( "fps" ).getAsFloat();
    }
}