package me.knighthat.youtubedl.response.format;

import lombok.Getter;
import lombok.experimental.Accessors;
import me.knighthat.youtubedl.exception.InsufficientElementsException;
import org.jetbrains.annotations.NotNull;

@Getter
@Accessors( chain = true, fluent = true )
public final class Mix extends Format implements Video, Audio {

    @NotNull
    private final String resolution;
    private final int    fps;
    private final int    samplingRate;
    @NotNull
    private final String vCodec;
    @NotNull
    private final String aCodec;

    /**
     * A class that represents this array
     * [18, mp4, 640x360, 360p, 595k, avc1.42001E, 24fps, mp4a.40.2 (44100Hz) (best)]
     *
     * @param arr must follow stated format
     *
     * @throws InsufficientElementsException when number of elements inside provided arr less than known template
     */
    public Mix( String @NotNull [] arr ) {
        super( Type.MIX, arr );

        if ( arr.length < 8 )
            throw new InsufficientElementsException( "Mix", arr.length, 8 );

        this.resolution = parseResolution( arr, 3 );
        this.fps = parseFps( arr, 6 );
        this.samplingRate = parseSamplingRate( arr, 7 );
        this.vCodec = arr[5];
        this.aCodec = arr[7].split( "\\s" )[0];
    }

    public Mix( int code, @NotNull String extension, int kbps, @NotNull String resolution, int fps, int samplingRate, @NotNull String vCodec, @NotNull String aCodec ) {
        super( Type.MIX, code, extension, kbps );
        this.resolution = resolution;
        this.fps = fps;
        this.samplingRate = samplingRate;
        this.vCodec = vCodec;
        this.aCodec = aCodec;
    }
}
