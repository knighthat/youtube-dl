package me.knighthat.youtubedl.command;

import me.knighthat.youtubedl.command.flag.Flag;
import me.knighthat.youtubedl.command.flag.GeoConfig;
import me.knighthat.youtubedl.command.flag.Header;
import me.knighthat.youtubedl.command.flag.UserAgent;
import me.knighthat.youtubedl.logging.Logger;
import me.knighthat.youtubedl.response.ListResponse;
import me.knighthat.youtubedl.response.subtitle.Subtitle;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.logging.Level;
import java.util.regex.Pattern;

public class Subtitles extends Command {

    @NotNull
    protected static final Pattern NO_SUBTITLE_PATTERN        = Pattern.compile( "\\w+ has no subtitles" );
    @NotNull
    protected static final Pattern HUMAN_SUBTITLE_PATTERN     = Pattern.compile( "Available subtitles for \\w+:" );

    public static @NotNull Builder builder( @NotNull String url ) { return new Builder( url ); }

    protected Subtitles( @NotNull String url, @NotNull Set<Flag> flags, @NotNull Set<Header> headers, @Nullable UserAgent userAgent, @Nullable GeoConfig geoConfig ) {
        super( url, flags, headers, userAgent, geoConfig );
        flags.add( Flag.noValue( "--list-subs" ) );
    }

    @Override
    public @NotNull ListResponse<Subtitle> execute() {
        List<String> outputs = super.outputs();
        Set<Subtitle> subtitles = new HashSet<>();

        /*
            Available subtitles for videoId:
            Language formats
            zh-Hans  vtt, ttml, srv3, srv2, srv1, json3
            en       vtt, ttml, srv3, srv2, srv1, json3
            hi       vtt, ttml, srv3, srv2, srv1, json3

            videoId has no subtitles
        */
        boolean hasCaption = false;
        for (int i = 0 ; i < outputs.size() ; i++) {
            // af       vtt, ttml, srv3, srv2, srv1, json3
            String output = outputs.get( i );

            if ( NO_SUBTITLE_PATTERN.matcher( output ).matches() )
                // Stop iteration if output is "videoId has no subtitles"
                break;
            if ( HUMAN_SUBTITLE_PATTERN.matcher( output ).matches() ) {
                /*
                Set isAutomatic if line matches "Available subtitles for videoId:"
                and skip "Language formats"
                */
                hasCaption = true;
                i++;
                continue;
            }

            if ( hasCaption ) {
                // ["af", "vtt, ttml, srv3, srv2, srv1, json3"]
                String[] parts = output.split( "(?<!,)\\s+" ); // Split string by spaces but not if it's right after coma
                // [vtt,  ttml,  srv3,  srv2,  srv1,  json3]
                String[] formats = parts[1].split( "," );

                for (String f : formats) {
                    try {
                        subtitles.add(
                          new Subtitle() {
                            @Override
                            public @NotNull String language() { return parts[0].trim(); }

                            @Override
                            public @NotNull Format format() { return Subtitle.Format.match( f.trim() ); }
                          }
                        );
                    } catch ( UnsupportedOperationException e ) {
                        Logger.exception( "failed to parse subtitle!", e, Level.WARNING );
                    }
                }
            }
        }

        return () -> List.copyOf( subtitles );
    }

    public static class Builder extends Command.Builder {
        
        protected Builder( @NotNull String url ) { super( url ); }

        @Override
        public @NotNull Builder flags( @NotNull Flag... flags ) { return (Builder) super.flags( flags ); }

        @Override
        public @NotNull Builder headers( @NotNull Header... headers ) { return (Builder) super.headers( headers ); }

        @Override
        public @NotNull Builder userAgent( @Nullable UserAgent userAgent ) { return (Builder) super.userAgent( userAgent ); }

        @Override
        public @NotNull Builder geoConfig( @Nullable GeoConfig geoConfig ) { return (Builder) super.geoConfig( geoConfig ); }

        @Override
        public @NotNull Subtitles build() { return new Subtitles( getUrl(), getFlags(), getHeaders(), getUserAgent(), getGeoConfig() ); }

        @Override
        public @NotNull ListResponse<Subtitle> execute() { return this.build().execute(); }
    }
}
