package me.knighthat.youtubedl;

import lombok.Getter;
import me.knighthat.youtubedl.command.*;
import me.knighthat.youtubedl.exception.UnsupportedVersionException;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class YoutubeDL {

    @NotNull
    private static final Pattern VERSION_PATTERN = Pattern.compile( "\\d+(\\.\\d+)*" );
    @NotNull
    private static final String  PYTHON_VERSION  = "3";
    @NotNull
    private static final String  YTDL_VERSION    = "2021.12.17";

    @NotNull
    @Getter
    private static String pythonPath = "python";
    @NotNull
    @Getter
    private static String ytdlPath   = "youtube-dl";

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

    private static @NotNull String getVersion( @NotNull String... command ) throws IOException {
        String version = null;

        Process process = new ProcessBuilder( command ).start();

        InputStream outputs = process.getInputStream();
        InputStreamReader reader = new InputStreamReader( outputs );
        BufferedReader bReader = new BufferedReader( reader );

        String line;
        while ((line = bReader.readLine()) != null) {
            Matcher matcher = VERSION_PATTERN.matcher( line );
            if ( matcher.find() ) {
                version = line.substring( matcher.start(), matcher.end() );
                break;
            }
        }

        bReader.close();
        reader.close();
        outputs.close();

        if ( version == null )
            throw new IOException( "Could not get version of " + command );
        else
            return version;
    }

    /**
     * Ensure that Python and Youtube-dl are
     * installed and their versions are supported
     * by this program.
     *
     * @throws IOException                 when error occurs during command execution
     * @throws UnsupportedVersionException when version is older than supported version
     */
    public static void init() throws IOException, UnsupportedVersionException {
        String pythonVersion = getVersion( pythonPath, "--version" );
        if ( !isVersionLaterThan( pythonVersion, PYTHON_VERSION ) )
            throw new UnsupportedVersionException( "Python", PYTHON_VERSION );

        String ytdlVersion = getVersion( pythonPath, ytdlPath, "--version" );
        if ( !isVersionLaterThan( ytdlVersion, YTDL_VERSION ) )
            throw new UnsupportedVersionException( "youtube-dl", YTDL_VERSION );
    }

    /**
     * Set the paths to python executable
     * and youtube-dl executable.
     * <p>
     * Leave blank or 'null' to use default value
     *
     * @param pythonPath path to python executable, blank or 'null' results in default 'python' command
     * @param ytdlPath   path to youtube-dl executable, blank or 'null' results in default 'youtube-dl' command
     *
     * @throws IOException if any error occurs during version verification
     */
    public static void init( @Nullable String pythonPath, @Nullable String ytdlPath ) throws IOException {
        if ( pythonPath != null && !pythonPath.isBlank() )
            YoutubeDL.pythonPath = pythonPath;
        if ( ytdlPath != null && !ytdlPath.isBlank() )
            YoutubeDL.ytdlPath = ytdlPath;
        init();
    }

    public static @NotNull Formats.Builder formats( @NotNull String url ) { return Formats.builder( url ); }

    public static @NotNull Thumbnails.Builder thumbnails( @NotNull String url ) { return Thumbnails.builder( url ); }

    public static @NotNull Stream.Builder stream( @NotNull String url ) { return Stream.builder( url ); }

    public static @NotNull Subtitles.Builder subtitles( @NotNull String url ) { return Subtitles.builder( url ); }

    public static @NotNull Json.Builder json( @NotNull String url ) { return Json.builder( url ); }

    public static @NotNull Video.Builder video( @NotNull String url ) { return Video.builder( url ); }
}