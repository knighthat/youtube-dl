package me.knighthat.extractor.asiancrush.command;

import me.knighthat.extractor.asiancrush.AsianCrush;
import me.knighthat.extractor.asiancrush.response.format.Movie;
import me.knighthat.internal.utils.FormatUtils;
import me.knighthat.youtubedl.command.flag.Flag;
import me.knighthat.youtubedl.command.flag.GeoConfig;
import me.knighthat.youtubedl.command.flag.Header;
import me.knighthat.youtubedl.command.flag.UserAgent;
import me.knighthat.youtubedl.response.ListResponse;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.regex.Pattern;

public class FormatsImpl extends me.knighthat.youtubedl.command.FormatsImpl implements Formats {

    @NotNull
    private static final Pattern FORMAT_PATTERN = Pattern.compile( "^\\s*hls-\\d+\\s+mp4\\s+\\d+x\\d+\\s+\\d+k\\s*,\\s*avc1\\.\\d+\\s*,\\s*\\d+\\.\\d+fps\\s*,\\s*mp4a\\.\\d+\\s*(\\(best\\))?$\n" );

    public static @NotNull Builder builder( @NotNull String url ) { return new Builder( url ); }

    private FormatsImpl(
        @NotNull String url,
        @NotNull Set<Flag> flags,
        @NotNull Set<Header> headers,
        @Nullable UserAgent userAgent,
        @Nullable GeoConfig geoConfig
    ) {
        super( url, flags, headers, userAgent, geoConfig );
    }

    @Override
    public @NotNull ListResponse<AsianCrush.Movie> execute() {
        List<AsianCrush.Movie> formats = new ArrayList<>();

        /*
        hls-361      mp4        426x240     361k , avc1.64001F, 23.976fps, mp4a.40.2
        hls-466      mp4        512x288     466k , avc1.64001F, 23.976fps, mp4a.40.2
        hls-3577     mp4        1920x1080  3577k , avc1.64002A, 23.976fps, mp4a.40.2 (best)
        */
        for (String output : super.outputs()) {
            if ( !FORMAT_PATTERN.matcher( output ).matches() )
                continue;

            /*
            Input: hls-361      mp4        426x240     361k ,  avc1.64001F,  23.976fps,  mp4a.40.2
            Output: [hls-361, mp4, 426x240, 361k,   avc1.64001F,   23.976fps,   mp4a.40.2]
             */
            String[] all = FormatUtils.split( output );

            formats.add( new Movie( all ) );
        }

        return () -> formats;
    }

    public static class Builder extends me.knighthat.youtubedl.command.FormatsImpl.Builder implements Formats.Builder {

        private Builder( @NotNull String url ) { super( url ); }

        @Override
        public @NotNull Builder flags( @NotNull Flag... flags ) {
            super.flags( flags );
            return this;
        }

        @Override
        public @NotNull Builder headers( @NotNull Header... headers ) {
            super.headers( headers );
            return this;
        }

        @Override
        public @NotNull Builder userAgent( @Nullable UserAgent userAgent ) {
            super.userAgent( userAgent );
            return this;
        }

        @Override
        public @NotNull Builder geoConfig( @Nullable GeoConfig geoConfig ) {
            super.geoConfig( geoConfig );
            return this;
        }

        @Override
        public @NotNull ListResponse<AsianCrush.Movie> execute() {
            return this.build().execute();
        }

        @Override
        public @NotNull Formats build() {
            return new FormatsImpl( getUrl(), getFlags(), getHeaders(), getUserAgent(), getGeoConfig() );
        }
    }
}