package me.knighthat.youtubedl.command;

import me.knighthat.youtubedl.YoutubeDL;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public class TestJson extends CommandTestTemplate {

    @Test
    @Override
    void testBuilder() {
        Json json = YoutubeDL.json( CommandTestTemplate.URL ).build();
        String[] actual = json.command();
        Set<String> actualSet = new HashSet<>( Arrays.asList( actual ) );

        String[] expected = {
                YoutubeDL.getPythonPath(),
                YoutubeDL.getYtdlPath(),
                "--dump-json", "",
                CommandTestTemplate.URL
        };
        Set<String> expectedSet = new HashSet<>( Arrays.asList( expected ) );

        Assertions.assertEquals( expected.length, actual.length );
        Assertions.assertEquals( expectedSet, actualSet );
    }
}
