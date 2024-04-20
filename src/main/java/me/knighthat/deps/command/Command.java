package me.knighthat.deps.command;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Unmodifiable;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class Command {

    /**
     * Execute a command, capture its outputs then return them as a list
     *
     * @param args command to execute chopped to multiple parts
     *
     * @return a list of captured output
     *
     * @throws IOException          when an error occurs while reading outputs
     * @throws InterruptedException when thread stops abruptly by outside factor
     */
    public static @NotNull @Unmodifiable List<String> captureOutput( @NotNull String... args ) throws IOException, InterruptedException {
        List<String> outputs = new ArrayList<>();

        Process process = new ProcessBuilder( args ).start();
        if ( process.waitFor() == 0 ) {

            InputStreamReader reader = new InputStreamReader( process.getInputStream() );
            BufferedReader bReader = new BufferedReader( reader );

            String line;
            while ((line = bReader.readLine()) != null)
                outputs.add( line );

            bReader.close();
            reader.close();

        }

        return List.copyOf( outputs );
    }
}
