package me.knighthat.youtubedl.command;

import me.knighthat.youtubedl.command.flag.Flag;
import me.knighthat.youtubedl.command.flag.GeoConfig;
import me.knighthat.youtubedl.command.flag.Header;
import me.knighthat.youtubedl.command.flag.UserAgent;
import me.knighthat.youtubedl.response.ListResponse;
import me.knighthat.youtubedl.response.format.AudioOnly;
import me.knighthat.youtubedl.response.format.Format;
import me.knighthat.youtubedl.response.format.Mix;
import me.knighthat.youtubedl.response.format.VideoOnly;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public final class Formats extends Command {

    public static @NotNull Builder builder( @NotNull String url ) { return new Builder( url ); }

    private Formats( @NotNull String url, @NotNull Set<Flag> flags, @NotNull Set<Header> headers, @NotNull UserAgent userAgent, @NotNull GeoConfig geoConfig ) {
        super( url, flags, headers, userAgent, geoConfig );
        flags().add( Flag.noValue( "--list-formats" ) );
    }

    @Override
    public @NotNull ListResponse<Format> execute() {
        List<Format> results = new ArrayList<>();
        for (String output : super.outputs()) {
            if ( !Character.isDigit( output.charAt( 0 ) ) )
                continue;

            /*
            Audio
            249          webm       audio only audio_quality_low   57k , webm_dash container, opus  (48000Hz), 2.48MiB

            Video
            248          webm       1080x1080  1080p   82k , webm_dash container, vp9, 25fps, video only, 3.59MiB

            Mix
            18           mp4        360x360    360p  169k , avc1.42001E, 25fps, mp4a.40.2 (44100Hz) (best)
            */
            Format.Type type = Format.Type.MIX;
            if ( output.contains( "video only" ) )
                type = Format.Type.VIDEO_ONLY;
            else if ( output.contains( "audio only" ) )
                type = Format.Type.AUDIO_ONLY;

            // [249          webm       audio only audio_quality_low   57k ,  webm_dash container,  opus  (48000Hz),  2.48MiB]
            String[] parts = output.trim().split( "," );
            // [249, webm, audio, only, audio_quality_low, 57k]
            String[] info = parts[0].trim().split( "\\s+" );

            String[] all = new String[parts.length + info.length - 1];
            System.arraycopy( info, 0, all, 0, info.length );
            System.arraycopy( parts, 1, all, info.length, parts.length - 1 );

            results.add(
                    switch (type) {
                        case VIDEO_ONLY -> new VideoOnly( all );
                        case AUDIO_ONLY -> new AudioOnly( all );
                        case MIX -> new Mix( all );
                    }
            );
        }

        return () -> results;
    }

    public static class Builder extends Command.Builder {

        private Builder( @NotNull String url ) { super( url ); }

        @Override
        public @NotNull Builder flags( @NotNull Flag... flags ) {
            super.addFlags( flags );
            return this;
        }

        @Override
        public @NotNull Builder headers( @NotNull Header... headers ) {
            super.addHeaders( headers );
            return this;
        }

        @Override
        public @NotNull Builder userAgent( @Nullable UserAgent userAgent ) {
            super.setUserAgent( userAgent );
            return this;
        }

        @Override
        public @NotNull Builder geoConfig( @Nullable GeoConfig geoConfig ) {
            super.setGeoConfig( geoConfig );
            return this;
        }

        @Override
        public @NotNull Formats build() { return new Formats( getUrl(), getFlags(), getHeaders(), getUserAgent(), getGeoConfig() ); }

        @Override
        public @NotNull ListResponse<Format> execute() { return this.build().execute(); }
    }
}
