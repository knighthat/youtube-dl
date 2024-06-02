package me.knighthat.youtubedl.command;

import java.util.Set;
import java.util.regex.Pattern;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import me.knighthat.youtubedl.command.flag.Flag;
import me.knighthat.youtubedl.command.flag.GeoConfig;
import me.knighthat.youtubedl.command.flag.Header;
import me.knighthat.youtubedl.command.flag.UserAgent;
import me.knighthat.youtubedl.response.ListResponse;
import me.knighthat.youtubedl.response.subtitle.Subtitle;

public abstract class SubtitlesImpl extends CommandImpl implements Subtitles {

    @NotNull
    protected static final Pattern NO_SUBTITLE_PATTERN        = Pattern.compile( "\\w+ has no subtitles" );
    @NotNull
    protected static final Pattern HUMAN_SUBTITLE_PATTERN     = Pattern.compile( "Available subtitles for \\w+:" );

    protected SubtitlesImpl(
        @NotNull String url, 
        @NotNull Set<Flag> flags, 
        @NotNull Set<Header> headers,
        @Nullable UserAgent userAgent, 
        @Nullable GeoConfig geoConfig
    ) {
        super(url, flags, headers, userAgent, geoConfig);
        flags.add( Flag.noValue( "--list-subs" ) );
    }

    public abstract static class Builder extends CommandImpl.Builder implements Subtitles.Builder {

        protected Builder( @NotNull String url ) { super( url ); }

        @Override
        public @NotNull Builder flags( @NotNull Flag... flags ) { return (Builder) super.flags( flags ); }

        @Override
        public @NotNull Builder headers( @NotNull Header... headers) { return (Builder) super.headers( headers ); }

        @Override
        public @NotNull Builder userAgent( @Nullable UserAgent userAgent ) { return (Builder) super.userAgent( userAgent ); }

        @Override
        public @NotNull Builder geoConfig( @Nullable GeoConfig geoConfig ) { return (Builder) super.geoConfig( geoConfig ); }

        @Override
        public @NotNull ListResponse<Subtitle> execute() { return this.build().execute(); }
    }
}