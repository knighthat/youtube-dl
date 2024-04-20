package me.knighthat.deps;

import lombok.Data;
import lombok.Getter;
import me.knighthat.deps.command.Command;
import me.knighthat.deps.exception.UnsupportedVersionException;
import me.knighthat.deps.response.format.AudioOnly;
import me.knighthat.deps.response.format.Mix;
import me.knighthat.deps.response.format.VideoOnly;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class YoutubeDL {

    @NotNull
    private static final String PYTHON_VERSION = "3";
    @NotNull
    private static final String YTDL_VERSION   = "2021.12.17";

    @NotNull
    @Getter
    private static String[] command = { "youtube-dl" };

    private static boolean isVersionLaterThan( @NotNull String curVer, @NotNull String reqVer ) {
        String[] curParts = curVer.split( "\\." );
        String[] reqParts = reqVer.split( "\\." );

        for (int i = 0 ; i < Math.min( curParts.length, reqParts.length ) ; i++) {
            try {
                int current = Integer.parseInt( curParts[i] );
                int required = Integer.parseInt( reqParts[i] );

                if ( current != required )
                    return current > required;
            } catch ( NumberFormatException ignored ) {
                return false;
            }
        }

        return curParts.length >= reqParts.length;
    }

    private static boolean verifyPython() throws IOException, InterruptedException {
        List<String> pyVerStr = Command.captureOutput( "python", "--version" );
        if ( pyVerStr.isEmpty() )
            return false;

        String pyVersion = pyVerStr.get( 0 ).split( "\\s" )[1];
        return isVersionLaterThan( pyVersion, PYTHON_VERSION );
    }

    private static boolean verifyYoutubeDL() throws IOException, InterruptedException {
        String[] command = Arrays.copyOf( YoutubeDL.command, YoutubeDL.command.length + 1 );
        command[command.length - 1] = "--version";

        List<String> ytdlVerStr = Command.captureOutput( command );
        return ytdlVerStr.size() > 0 && isVersionLaterThan( ytdlVerStr.get( 0 ), YTDL_VERSION );
    }

    /**
     * Ensure that Python and Youtube-dl are
     * installed and their versions are supported
     * by this program.
     *
     * @throws IOException                 when error occurs during command execution
     * @throws InterruptedException        when thread stops abruptly by outside factor
     * @throws UnsupportedVersionException when version is older than supported version
     */
    public static void init() throws IOException, UnsupportedVersionException, InterruptedException {
        if ( !verifyPython() )
            throw new UnsupportedVersionException( "Python version " + PYTHON_VERSION + " is required to run this program!" );

        if ( !verifyYoutubeDL() )
            throw new UnsupportedVersionException( "Youtube-dl requires version new than " + YTDL_VERSION );
    }

    /**
     * Set the path to youtube-dl before validating.
     * <p>
     * Ensure that Python and Youtube-dl are
     * installed and their versions are supported
     * by this program.
     *
     * @param customPath path to __main__.py of youtube-dl
     *
     * @throws IOException                 when error occurs during command execution
     * @throws InterruptedException        when thread stops abruptly by outside factor
     * @throws UnsupportedVersionException when version is older than supported version
     */
    public static void init( @NotNull String... customPath ) throws IOException, InterruptedException, UnsupportedVersionException {
        command = customPath;
        init();
    }

    public static @NotNull YoutubeDL.YtdlCommand command( @NotNull String url ) { return new YtdlCommand( url ); }

    @Data
    public static class YtdlCommand {

        @NotNull
        private final String       url;
        @NotNull
        private final List<String> flags;

        private YtdlCommand( @NotNull String url ) {
            this.url = url;
            this.flags = new ArrayList<>();
        }

        public @NotNull YtdlCommand getFormats() {
            flags.add( "-F" );
            return this;
        }

        public @NotNull List<String> execute() throws IOException, InterruptedException {
            List<String> arguments = new ArrayList<>( Arrays.asList( YoutubeDL.getCommand() ) );
            arguments.addAll( flags );
            arguments.add( url );

            return me.knighthat.deps.command.Command.captureOutput( arguments.toArray( String[]::new ) );
        }
    }

    public static class Format {

        public static @NotNull Format url( @NotNull String url ) {
            return new Format( url );
        }

        private final String url;

        private Format( String url ) {
            this.url = url;
        }

        public @NotNull List<me.knighthat.deps.response.format.Format> execute() throws IOException, InterruptedException {
            List<me.knighthat.deps.response.format.Format> formats = new ArrayList<>();

            YtdlCommand ytdlCommand = YoutubeDL.command( url ).getFormats();

            for (String output : ytdlCommand.execute()) {
                if ( !Character.isDigit( output.charAt( 0 ) ) )
                    continue;

                String[] parts = output.trim().split( "," );
                String[] info = parts[0].trim().split( "\\s+" );

                /*
                    Video
                    [400, mp4, 2560x1440, 1440p, 5314k, mp4_dash container, av01.0.12M.08, 24fps, video only, 152.20MiB]

                    Audio
                    [140, m4a, audio, only, audio_quality_medium, 129k, m4a_dash container, mp4a.40.2 (44100Hz), 3.71MiB]

                    Mix
                    [18, mp4, 640x360, 360p, 595k, avc1.42001E, 24fps, mp4a.40.2 (44100Hz) (best)]
                */
                me.knighthat.deps.response.format.Format.Type type = me.knighthat.deps.response.format.Format.Type.MIX;

                String[] all = new String[parts.length + info.length - 1];
                for (int i = 0 ; i < info.length ; i++) {
                    String part = info[i].trim();
                    if ( part.equals( "audio" ) )
                        type = me.knighthat.deps.response.format.Format.Type.AUDIO_ONLY;
                    all[i] = part;
                }
                for (int i = 1 ; i < parts.length ; i++) {
                    String part = parts[i].trim();
                    if ( part.equals( "video only" ) )
                        type = me.knighthat.deps.response.format.Format.Type.VIDEO_ONLY;
                    all[i + info.length - 1] = part;
                }

                formats.add(
                        switch (type) {
                            case VIDEO_ONLY -> new VideoOnly( all );
                            case AUDIO_ONLY -> new AudioOnly( all );
                            case MIX -> new Mix( all );
                        }
                );
            }

            return formats;
        }
    }
}