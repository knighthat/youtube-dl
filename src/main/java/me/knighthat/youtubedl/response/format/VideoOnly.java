package me.knighthat.youtubedl.response.format;

import lombok.Getter;
import lombok.experimental.Accessors;
import me.knighthat.youtubedl.exception.InsufficientElementsException;
import org.jetbrains.annotations.NotNull;

@Getter
@Accessors( chain = true, fluent = true )
public final class VideoOnly extends SizedMedia implements Video {

    @NotNull
    private final String resolution;
    private final int    fps;
    @NotNull
    private final String vCodec;

    /**
     * A class that represents this array
     * [400, mp4, 2560x1440, 1440p, 5314k, mp4_dash container, av01.0.12M.08, 24fps, video only, 152.20MiB]
     *
     * @param arr must follow stated format
     *
     * @throws InsufficientElementsException when number of elements inside provided arr less than known template
     */
    public VideoOnly( String @NotNull [] arr ) throws InsufficientElementsException {
        super( Type.VIDEO_ONLY, arr );

        if ( arr.length < 10 )
            throw new InsufficientElementsException( "VideoOnly", arr.length, 10 );

        this.resolution = parseResolution( arr, 3 );
        this.fps = parseFps( arr, 7 );
        this.vCodec = arr[6];
    }

    public VideoOnly( int code, @NotNull String extension, int kbps, float size, @NotNull String resolution, int fps, @NotNull String vCodec ) {
        super( Type.VIDEO_ONLY, code, extension, kbps, size );
        this.resolution = resolution;
        this.fps = fps;
        this.vCodec = vCodec;
    }
}
