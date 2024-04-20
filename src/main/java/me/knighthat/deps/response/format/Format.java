package me.knighthat.deps.response.format;

import lombok.Getter;
import lombok.ToString;
import me.knighthat.deps.exception.PatternMismatchException;
import org.jetbrains.annotations.NotNull;

import java.util.regex.Pattern;

@Getter
@ToString
public abstract class Format {

    @NotNull
    static final Pattern QUALITY_PATTERN = Pattern.compile( "^\\d+k$" );
    @NotNull
    static final Pattern SIZE_PATTERN    = Pattern.compile( "^\\d+(\\.\\d+)?[KMGTPEZY]?iB$" );


    @NotNull
    @ToString.Exclude
    protected final Type   type;
    protected final int    code;
    @NotNull
    protected final String extension;
    @NotNull
    protected final String encoder;
    protected final int    kbps;
    protected final float  size;

    protected Format( @NotNull Type type, String @NotNull [] arr ) throws PatternMismatchException {
        /*
            Video
            [400, mp4, 2560x1440, 1440p, 5314k, mp4_dash container, av01.0.12M.08, 24fps, video only, 152.20MiB]

            Audio
            [140, m4a, audio, only, audio_quality_medium, 129k, m4a_dash container, mp4a.40.2 (44100Hz), 3.71MiB]

            Mix
            [18, mp4, 640x360, 360p, 595k, avc1.42001E, 24fps, mp4a.40.2 (44100Hz) (best)]
        */
        this.type = type;
        this.code = Integer.parseInt( arr[0] );
        this.extension = arr[1];
        this.encoder = type == Type.VIDEO_ONLY ? arr[6] : arr[7].split( "\\s" )[0];

        String kbps = arr[type == Type.AUDIO_ONLY ? 5 : 4];
        if ( !QUALITY_PATTERN.matcher( kbps ).matches() )
            throw new PatternMismatchException( kbps, "quality", arr );
        else {
            kbps = kbps.substring( 0, kbps.length() - 1 );
            this.kbps = Integer.parseInt( kbps );
        }

        if ( type != Type.MIX ) {
            String sizeStr = arr[arr.length - 1];
            if ( !SIZE_PATTERN.matcher( sizeStr ).matches() )
                throw new PatternMismatchException( sizeStr, "file's size", arr );
            else {
                sizeStr = sizeStr.substring( 0, sizeStr.length() - 3 );
                this.size = Float.parseFloat( sizeStr );
            }
        } else
            this.size = 0;
    }

    public enum Type {
        VIDEO_ONLY,
        AUDIO_ONLY,
        MIX
    }
}
