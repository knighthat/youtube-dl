package me.knighthat.extractor.asiancrush.command;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.regex.Pattern;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import me.knighthat.extractor.asiancrush.response.format.Mix;
import me.knighthat.youtubedl.command.FormatsImpl;
import me.knighthat.youtubedl.command.flag.Flag;
import me.knighthat.youtubedl.command.flag.GeoConfig;
import me.knighthat.youtubedl.command.flag.Header;
import me.knighthat.youtubedl.command.flag.UserAgent;
import me.knighthat.youtubedl.response.ListResponse;
import me.knighthat.youtubedl.response.format.Format;

/**
 * Formats extractor for AsianCrush website.
 */
public class Formats extends FormatsImpl {

    @NotNull
    private static final Pattern FORMAT_PATTERN = Pattern.compile( "^\\s*hls-\\d+\\s+mp4\\s+\\d+x\\d+\\s+\\d+k\\s*,\\s*avc1\\.\\d+\\s*,\\s*\\d+\\.\\d+fps\\s*,\\s*mp4a\\.\\d+\\s*(\\(best\\))?$\n" );

    public static @NotNull Builder builder( @NotNull String url ) { return new Builder( url ); }

    protected Formats(
        @NotNull String url, 
        @NotNull Set<Flag> flags, 
        @NotNull Set<Header> headers,
        @Nullable UserAgent userAgent, 
        @Nullable GeoConfig geoConfig
    ) {
        super(url, flags, headers, userAgent, geoConfig);
    }

    @Override
    public @NotNull ListResponse<Format> execute() {
        List<Format> formats = new ArrayList<>();

        /*
        hls-361      mp4        426x240     361k , avc1.64001F, 23.976fps, mp4a.40.2
        hls-466      mp4        512x288     466k , avc1.64001F, 23.976fps, mp4a.40.2
        hls-3577     mp4        1920x1080  3577k , avc1.64002A, 23.976fps, mp4a.40.2 (best)
        */
        for ( String output : super.outputs() ) {
            if ( !FORMAT_PATTERN.matcher( output ).matches() )
                continue;

            // [hls-361      mp4        426x240     361k ,  avc1.64001F,  23.976fps,  mp4a.40.2]
            String[] parts = output.trim().split( "," );
            // [hls-361, mp4, 426x240, 361k]
            String[] info = parts[0].trim().split( "\\s+" );

            String[] all = new String[parts.length + info.length - 1];
            System.arraycopy( info, 0, all, 0, info.length );
            System.arraycopy( parts, 1, all, info.length, parts.length - 1 );

            formats.add( new Mix( all ) );
        }

        return () -> formats;
    }

    public static class Builder extends FormatsImpl.Builder {

        protected Builder( @NotNull String url ) { super( url ); }

        @Override
        public @NotNull Formats build() {
            return new Formats( getUrl(), getFlags(), getHeaders(), getUserAgent(), getGeoConfig() );
        }
    }
}