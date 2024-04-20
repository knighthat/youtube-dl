package me.knighthat.deps;

import lombok.Getter;
import me.knighthat.deps.command.Command;
import me.knighthat.deps.command.Formats;
import me.knighthat.deps.exception.UnsupportedVersionException;
import me.knighthat.deps.response.format.Format;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
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
            throw new UnsupportedVersionException( "Python", PYTHON_VERSION );

        if ( !verifyYoutubeDL() )
            throw new UnsupportedVersionException( "youtube-dl", YTDL_VERSION );
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

    public static @NotNull Formats.Builder formats( @NotNull String url ) {
        return Formats.builder( url );
    }

    public static void main( String[] args ) throws IOException, InterruptedException {
        init( "python", "/mnt/projects/Java/website/knighthat-me/youtube_dl/__main__.py" );

        List<Format> responseFormats = YoutubeDL.formats( "https://www.youtube.com/watch?v=YqZfJRu-_zY" ).execute();
        for (Format response : responseFormats)
            System.out.println( response );

    }
}