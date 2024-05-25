package me.knighthat.extractor.youtube.command;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import org.jetbrains.annotations.NotNull;

import me.knighthat.youtubedl.YoutubeDL;
import me.knighthat.youtubedl.command.Command;

abstract class CommandTestTemplate {

    @NotNull
    static String URL = "https://youtube.com/watch?v=12312312312";

    protected abstract void testBuilderInit();

    protected abstract void testBuilderAddFlags();

    protected abstract void testBuilderAddHeaders();

    protected abstract void testBuilderAddUserAgent();

    protected abstract void testBuilderAddGeoConfig();

    protected @NotNull Set<String> cmdToSet( @NotNull Command command ) {
        return new HashSet<>( Arrays.asList( command.command() ) );
    }

    protected @NotNull Set<String> commandContains( @NotNull String... args ) {
        String[] result = new String[ args.length + 3 ];
        result[0] = YoutubeDL.getPythonPath();
        result[1] = YoutubeDL.getYtdlPath();
        System.arraycopy( args, 0, result, 2, args.length );
        result[result.length - 1] = URL;

        return new HashSet<>( Arrays.asList( result ) );
    }
}
