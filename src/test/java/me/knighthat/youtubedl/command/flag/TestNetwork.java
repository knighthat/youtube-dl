package me.knighthat.youtubedl.command.flag;

import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public class TestNetwork {

    @NotNull
    private static final String PROXY_URL      = "https://example.com";
    @NotNull
    private static final String SOURCE_ADDRESS = "255.255.255.255";
    private static final int    TIMEOUT        = 5;

    @Test
    public void testProxy() {
        Network network = Network.builder().proxy( PROXY_URL ).build();
        String[] actual = network.flags();
        String[] expected = { "--proxy", PROXY_URL };
        Assertions.assertArrayEquals( expected, actual );
    }

    @Test
    public void testTimeout() {
        Network network = Network.builder().timeout( TIMEOUT ).build();
        String[] actual = network.flags();
        String[] expected = { "--socket-timeout", String.valueOf( TIMEOUT ) };
        Assertions.assertArrayEquals( expected, actual );
    }

    @Test
    public void testSourceAddress() {
        Network network = Network.builder().sourceIp( SOURCE_ADDRESS ).build();
        String[] actual = network.flags();
        String[] expected = { "--source-address", SOURCE_ADDRESS };
        Assertions.assertArrayEquals( expected, actual );
    }

    @Test
    public void testForceIPv4Switch() {
        Network network = Network.builder().forceIPv4().build();
        String[] actual = network.flags();
        String[] expected = { "-4", "" };
        Assertions.assertArrayEquals( expected, actual );
    }

    @Test
    public void testForceIPv6Switch() {
        Network network = Network.builder().forceIPv6().build();
        String[] actual = network.flags();
        String[] expected = { "-6", "" };
        Assertions.assertArrayEquals( expected, actual );
    }

    @Test
    public void testForceIPConflict() {
        Assertions.assertThrows(
                IllegalArgumentException.class,
                () -> Network.builder().forceIPv4().forceIPv6().build()
        );
    }

    @Test
    public void testAllValues() {
        Network network = Network.builder()
                                 .proxy( PROXY_URL )
                                 .timeout( TIMEOUT )
                                 .sourceIp( SOURCE_ADDRESS )
                                 .forceIPv4()
                                 .build();
        String[] actual = network.flags();
        Set<String> actualSet = new HashSet<>( Arrays.asList( actual ) );

        String[] expected = {
                "--proxy", PROXY_URL,
                "--socket-timeout", String.valueOf( TIMEOUT ),
                "--source-address", SOURCE_ADDRESS,
                "-4", ""
        };
        Set<String> expectedSet = new HashSet<>( Arrays.asList( expected ) );

        Assertions.assertEquals( expected.length, actual.length );
        Assertions.assertEquals( expectedSet, actualSet );
    }
}
