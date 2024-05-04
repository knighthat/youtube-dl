package me.knighthat.youtubedl.command.flag;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class TestDownload {

    private static final int RATE = 10;
    private static final int RETRIES = 5;
    
    @Test
    public void testRate() {
        Download download = Download.builder().rate(RATE).build();
        String[] actual = download.flags();
        Set<String> actualSet = new HashSet<>(Arrays.asList(actual));
        
        String[] expected = {
            "--limit-rate", String.valueOf(RATE)
        };
        Set<String> expectedSet = new HashSet<>(Arrays.asList(expected));

        Assertions.assertEquals(expected.length, actual.length);
        Assertions.assertEquals(expectedSet, actualSet);
    }
    
    @Test
    public void testRetries() {
        Download download = Download.builder().retries(RETRIES).build();
        String[] actual = download.flags();
        Set<String> actualSet = new HashSet<>(Arrays.asList(actual));
        
        String[] expected = {
            "--retries", String.valueOf(RETRIES)
        };
        Set<String> expectedSet = new HashSet<>(Arrays.asList(expected));

        Assertions.assertEquals(expected.length, actual.length);
        Assertions.assertEquals(expectedSet, actualSet);
    }
}
