package me.knighthat.youtubedl.command;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import me.knighthat.extractor.youtube.response.subtitle.DownloadableSubtitle;
import me.knighthat.extractor.youtube.response.subtitle.Subtitle;
import me.knighthat.youtubedl.command.flag.Flag;
import me.knighthat.youtubedl.command.flag.GeoConfig;
import me.knighthat.youtubedl.command.flag.Header;
import me.knighthat.youtubedl.command.flag.UserAgent;
import me.knighthat.youtubedl.exception.UnsupportedSubtitleFormatException;
import me.knighthat.youtubedl.logging.Logger;
import me.knighthat.youtubedl.response.OptionalResponse;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.text.SimpleDateFormat;
import java.util.HashSet;
import java.util.Set;
import java.util.logging.Level;

public abstract class Video extends Command {

    @NotNull
    private static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat( "yyyyMMdd" );

    private static @NotNull Set<DownloadableSubtitle> subtitleSet( @NotNull JsonObject json, boolean isAutomatic ) {
        Set<DownloadableSubtitle> subtitles = new HashSet<>();

        for (String language : json.keySet()) {
            for (JsonElement formatKey : json.getAsJsonArray(language)) {
                JsonObject jsonFormat = formatKey.getAsJsonObject();

                try {   
                    String ext = jsonFormat.get("ext").getAsString();
                    Subtitle.Format format = Subtitle.Format.match(ext);

                    subtitles.add(
                        new DownloadableSubtitle() {
                            @Override
                            public @NotNull String language() { return language; }

                            @Override
                            public @NotNull Format format() { return format; }

                            @Override
                            public boolean isAutomatic() { return isAutomatic; }

                            @Override
                            public @NotNull String url() { return jsonFormat.get("url").getAsString(); }  
                        }
                    );
                } catch (UnsupportedSubtitleFormatException e) {
                    Logger.exception("failed to parse subtitle!", e, Level.WARNING);
                }
            }
        }

        return subtitles;
    }

    protected Video( @NotNull String url, @NotNull Set<Flag> flags, @NotNull Set<Header> headers, @Nullable UserAgent userAgent, @Nullable GeoConfig geoConfig ) {
        super( url, flags, headers, userAgent, geoConfig );
    }

    @Override
    public abstract @NotNull OptionalResponse<me.knighthat.youtubedl.response.video.Video> execute();

    public abstract static class Builder extends Command.Builder {
        
        protected Builder( @NotNull String url ) { super( url ); }

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
            super.setGeoConfig( getGeoConfig() );
            return this;
        }

        @Override
        public abstract @NotNull Video build();

        @Override
        public @NotNull OptionalResponse<me.knighthat.youtubedl.response.video.Video> execute() { return this.build().execute(); }
    }
}
