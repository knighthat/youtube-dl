package me.knighthat.youtubedl.command.flag;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public class TestHeader {

    @Test
    public void testSingleKeyValue() {
        Header header = Header.key( "Content-Type" ).value( "application/json" );
        String[] actual = header.flags();
        Set<String> actualSet = new HashSet<>( Arrays.asList( actual ) );

        String[] expected = { "--add-header", "Content-Type:application/json" };
        Set<String> expectedSet = new HashSet<>( Arrays.asList( expected ) );

        Assertions.assertEquals( expected.length, actual.length );
        Assertions.assertEquals( expectedSet, actualSet );
    }

    @Test
    public void testKeyPairsChaining() {
        Header header = (Header) Header.chain()
                                       .key( "Authorization" ).value( "Bearer <access_token>" )
                                       .key( "Cookie" ).value( "sessionid=abc123; csrftoken=xyz456" )
                                       .key( "Content-Type" ).value( "application/json" )
                                       .build();
        String[] actual = header.flags();
        Set<String> actualSet = new HashSet<>( Arrays.asList( actual ) );

        String[] expected = {
                "--add-header", "Authorization:Bearer <access_token>",
                "--add-header", "Cookie:sessionid=abc123; csrftoken=xyz456",
                "--add-header", "Content-Type:application/json"
        };
        Set<String> expectedSet = new HashSet<>( Arrays.asList( expected ) );

        Assertions.assertEquals( expected.length, actual.length );
        Assertions.assertEquals( expectedSet, actualSet );
    }
}
