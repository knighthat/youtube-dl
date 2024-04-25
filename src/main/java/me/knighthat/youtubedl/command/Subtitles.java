package me.knighthat.youtubedl.command;

import me.knighthat.youtubedl.command.flag.CommandFlag;
import me.knighthat.youtubedl.command.flag.GeoConfig;
import me.knighthat.youtubedl.command.flag.HttpHeader;
import me.knighthat.youtubedl.command.flag.UserAgent;
import me.knighthat.youtubedl.response.ListResponse;
import me.knighthat.youtubedl.response.subtitle.Subtitle;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.IOException;
import java.util.*;
import java.util.logging.Logger;
import java.util.regex.Pattern;

public class Subtitles extends YtdlCommand {

    @NotNull
    private static final Pattern NO_SUBTITLE_PATTERN        = Pattern.compile( "\\w+ has no subtitles" );
    @NotNull
    private static final Pattern HUMAN_SUBTITLE_PATTERN     = Pattern.compile( "Available subtitles for \\w+:" );
    @NotNull
    private static final Pattern AUTOMATIC_SUBTITLE_PATTERN = Pattern.compile( "Available automatic captions for \\w+:" );

    public static @NotNull Builder builder( @NotNull String url ) { return new Builder( url ); }

    Subtitles( @NotNull String url, @NotNull Set<CommandFlag> flags, @NotNull Set<HttpHeader> headers, @NotNull UserAgent userAgent, @Nullable GeoConfig geoConfig ) {
        super( url, flags, headers, userAgent, geoConfig );
    }

    private @NotNull Set<Subtitle> genSubtitle( @NotNull String str, boolean isAutomatic ) {
        Set<Subtitle> subtitles = new HashSet<>();

        // af       vtt, ttml, srv3, srv2, srv1, json3
        String[] parts = str.split( "\\s+" );
        String[] formats = parts[1].split( "," );

        for (String f : formats) {
            try {
                Subtitle.Format format = Subtitle.Format.match( f );
                subtitles.add( new Subtitle( parts[0], format, isAutomatic ) );
            } catch ( UnsupportedOperationException e ) {
                Logger.getLogger( "YoutubeDL" ).warning( e.getMessage() );
            }
        }

        return subtitles;
    }

    @Override
    public @NotNull Results execute() {
        List<Subtitle> results = new ArrayList<>();
        List<String> outputs = new ArrayList<>();
        try {
            outputs = super.execute0();
        } catch ( IOException | InterruptedException ignored ) {
        }

        /*
            Available automatic captions for videoId:
            Language formats
            af       vtt, ttml, srv3, srv2, srv1, json3
            ak       vtt, ttml, srv3, srv2, srv1, json3
            sq       vtt, ttml, srv3, srv2, srv1, json3

            Available subtitles for videoId:
            Language formats
            zh-Hans  vtt, ttml, srv3, srv2, srv1, json3
            en       vtt, ttml, srv3, srv2, srv1, json3
            hi       vtt, ttml, srv3, srv2, srv1, json3

            videoId has no subtitles
         */
        Boolean isAutomatic = null;
        for (int i = 1 ; i < outputs.size() ; i++) {
            String output = outputs.get( i );
            System.out.println( output );

            if ( NO_SUBTITLE_PATTERN.matcher( output ).matches() )
                break;
            if ( AUTOMATIC_SUBTITLE_PATTERN.matcher( output ).matches() ) {
                System.out.println( "Match automatic" );
                isAutomatic = true;
                i++;
                continue;
            } else if ( HUMAN_SUBTITLE_PATTERN.matcher( output ).matches() ) {
                System.out.println( "Match human" );
                isAutomatic = false;
                i++;
                continue;
            }
            if ( isAutomatic != null ) {
                results.addAll( genSubtitle( output, isAutomatic ) );
            }
        }

        return new Results( results );
    }

    @Override
    protected String @NotNull [] command() {
        flags.add( CommandFlag.noValue( "--list-subs" ) );
        return super.command();
    }

    public static class Builder extends YtdlCommand.Builder {

        private Builder( @NotNull String url ) { super( url ); }

        public @NotNull Builder flags( @NotNull CommandFlag... flags ) {
            super.flags().addAll( Arrays.asList( flags ) );
            return this;
        }

        public @NotNull Builder headers( @NotNull HttpHeader... headers ) {
            super.headers().addAll( Arrays.asList( headers ) );
            return this;
        }

        public @NotNull Builder userAgent( @NotNull UserAgent userAgent ) {
            super.userAgent( userAgent );
            return this;
        }

        public @NotNull Builder geoConfig( @NotNull GeoConfig geoConfig ) {
            super.geoConfig( geoConfig );
            return this;
        }

        @Override
        public @NotNull Subtitles build() { return new Subtitles( url(), flags(), headers(), userAgent(), geoConfig() ); }

        @Override
        public @NotNull Results execute() { return this.build().execute(); }
    }

    public record Results( @NotNull List<Subtitle> items ) implements ListResponse<Subtitle> {
    }
}
