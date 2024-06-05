package me.knighthat.extractor.youtube.command;

import me.knighthat.extractor.youtube.response.format.Audio;
import me.knighthat.extractor.youtube.response.format.Mix;
import me.knighthat.extractor.youtube.response.format.Video;
import me.knighthat.internal.utils.FormatUtils;
import me.knighthat.youtubedl.command.FormatsImpl;
import me.knighthat.youtubedl.command.flag.Flag;
import me.knighthat.youtubedl.command.flag.GeoConfig;
import me.knighthat.youtubedl.command.flag.Header;
import me.knighthat.youtubedl.command.flag.UserAgent;
import me.knighthat.youtubedl.response.ListResponse;
import me.knighthat.youtubedl.response.format.Format;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class Formats extends FormatsImpl {

    public static @NotNull Builder builder( @NotNull String url ) { return new Builder( url ); }

    private Formats(
        @NotNull String url,
        @NotNull Set<Flag> flags,
        @NotNull Set<Header> headers,
        @Nullable UserAgent userAgent,
        @Nullable GeoConfig geoConfig
    ) {
        super( url, flags, headers, userAgent, geoConfig );
    }

    @Override
    public @NotNull ListResponse<Format> execute() {
        List<Format> formats = new ArrayList<>();

        /*
        Audio
        249          webm       audio only audio_quality_low   57k , webm_dash container, opus  (48000Hz), 2.48MiB

        Video
        248          webm       1080x1080  1080p   82k , webm_dash container, vp9, 25fps, video only, 3.59MiB

        Mix
        18           mp4        360x360    360p  169k , avc1.42001E, 25fps, mp4a.40.2 (44100Hz) (best)
        */
        for (String output : super.outputs()) {
            if ( !Character.isDigit( output.charAt( 0 ) ) )
                continue;

            /*
            Input: 249          webm       audio only audio_quality_low   57k ,  webm_dash container,  opus  (48000Hz),  2.48MiB
            Output: [249, webm, audio, only, audio_quality_low, 57k,   webm_dash container,   opus  (48000Hz),   2.48MiB]
             */
            String[] all = FormatUtils.split( output );
            
            Format format;
            if ( output.contains( "video only" ) )
                format = new Video( all );
            else if ( output.contains( "audio only" ) )
                format = new Audio( all );
            else
                format = new Mix( all );
            formats.add( format );
        }

        return () -> formats;
    }

    public static class Builder extends FormatsImpl.Builder {

        private Builder( @NotNull String url ) { super( url ); }

        @Override
        public @NotNull Formats build() {
            return new Formats( getUrl(), getFlags(), getHeaders(), getUserAgent(), getGeoConfig() );
        }
    }
}