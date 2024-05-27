package me.knighthat.youtubedl;

import lombok.Getter;
import me.knighthat.youtubedl.exception.UnsupportedVersionException;
import org.jetbrains.annotations.NotNull;

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
    private static final String  YTDL_VERSION    = "2021.12.17";

    @NotNull
    @Getter
    private static String ytdlPath = "youtube-dl";

    /**
     * Ensure that Python and Youtube-dl are
     * installed and their versions are supported
     * by this program.
     *
     * @throws IOException                 when error occurs during command execution
     * @throws UnsupportedVersionException when version is older than supported version
     */
    public static void init() throws IOException, UnsupportedVersionException {
        /*
        * Get youtube-dl's version
        */
        String version = null;

        Process process = new ProcessBuilder( ytdlPath, "--version" ).start();
        try (
            InputStream outputs = process.getInputStream();
            InputStreamReader reader = new InputStreamReader( outputs );
            BufferedReader bReader = new BufferedReader( reader )
        ) {
            String line;
            while ((line = bReader.readLine()) != null) {
                Matcher matcher = VERSION_PATTERN.matcher( line );
                if ( matcher.find() ) {
                    version = line.substring( matcher.start(), matcher.end() );
                    break;
                }
            }
        }

        /*
        * Verify youtube-dl's version
        */
        String[] curParts = version.split( "\\." );
        String[] reqParts = YTDL_VERSION.split( "\\." );
        for (int i = 0 ; i < Math.min( curParts.length, reqParts.length ) ; i++) {
            int current = Integer.parseInt( curParts[i] );
                int required = Integer.parseInt( reqParts[i] );

                if ( current > required )
                    break;
                else if ( current < required )
                    throw new UnsupportedVersionException( "youtube-dl", YTDL_VERSION );
        }
    }

    /**
     * Set the path you youtube-dl executable.
     * 
     * @param ytdlPath path to youtube-dl executable
     * @throws UnsupportedVersionException when version of youtube-dl is not supported
     * @throws IOException if any error occurs during version verification
     */
    public static void init( @NotNull String ytdlPath ) throws UnsupportedVersionException, IOException {
        if ( ytdlPath.isBlank() )
            throw new IllegalArgumentException( "youtube-dl path cannot be empty!" );
        YoutubeDL.ytdlPath = ytdlPath;
        init();
    }
}