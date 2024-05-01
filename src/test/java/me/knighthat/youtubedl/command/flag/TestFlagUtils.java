package me.knighthat.youtubedl.command.flag;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class TestFlagUtils {

    @Test
    public void testMapToArray() {
        Map<String, String> map = Map.of(
                "--proxy", "https://example.com",
                "--socket-timeout", "10",
                "--source-address", "255.255.255.255",
                "-4", "",
                "-6", ""
        );
        String[] converted = FlagUtils.mapToArray( map );
        Set<String> acutalSet = new HashSet<>( Arrays.asList( converted ) );

        String[] expected = {
                "--proxy", "https://example.com",
                "--socket-timeout", "10",
                "--source-address", "255.255.255.255",
                "-4", "",
                "-6", ""
        };
        Set<String> expectedSet = new HashSet<>( Arrays.asList( expected ) );

        Assertions.assertEquals( expected.length, converted.length );
        Assertions.assertEquals( expectedSet, acutalSet );
    }
}
