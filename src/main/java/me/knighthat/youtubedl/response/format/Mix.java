package me.knighthat.youtubedl.response.format;

import lombok.Getter;
import me.knighthat.youtubedl.exception.InsufficientElementsException;
import org.jetbrains.annotations.NotNull;

@Getter
public final class Mix extends Format implements Video, Audio {

    @NotNull
    private final String resolution;
    private final int    fps;
    private final int    samplingRate;

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
    }
}
