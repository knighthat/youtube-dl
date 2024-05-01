package me.knighthat.youtubedl.command.flag;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Locale;
import java.util.Set;

public class TestGeoConfig {

    @Test
    public void testDefaultCountryCode() {
        GeoConfig geoConfig = GeoConfig.builder().build();
        String[] actual = geoConfig.flags();
        Set<String> actualSet = new HashSet<>( Arrays.asList( actual ) );

        String countryCode = Locale.getDefault().getCountry();
        String[] expected = { "--geo-bypass-country", countryCode };
        Set<String> expectedSet = new HashSet<>( Arrays.asList( expected ) );

        Assertions.assertEquals( expected.length, actual.length );
        Assertions.assertEquals( expectedSet, actualSet );
    }

    @Test
    public void testBypassSwitch() {
        GeoConfig geoConfig = GeoConfig.builder().bypass().build();
        String[] actual = geoConfig.flags();
        Set<String> actualSet = new HashSet<>( Arrays.asList( actual ) );

        String countryCode = Locale.getDefault().getCountry();
        String[] expected = { "--geo-bypass-country", countryCode, "--geo-bypass", "" };
        Set<String> expectedSet = new HashSet<>( Arrays.asList( expected ) );

        Assertions.assertEquals( expected.length, actual.length );
        Assertions.assertEquals( expectedSet, actualSet );
    }

    @Test
    public void testNonDefaultCountryCode() {
        String countryCode = "FR";

        GeoConfig geoConfig = GeoConfig.builder().countryCode( countryCode ).build();
        String[] actual = geoConfig.flags();
        Set<String> actualSet = new HashSet<>( Arrays.asList( actual ) );

        String[] expected = { "--geo-bypass-country", countryCode };
        Set<String> expectedSet = new HashSet<>( Arrays.asList( expected ) );

        Assertions.assertEquals( expected.length, actual.length );
        Assertions.assertEquals( expectedSet, actualSet );
        Assertions.assertFalse( actualSet.contains( "--geo-bypass" ) );
    }

    @Test
    public void testInvalidCountryCode() {
        Assertions.assertThrows( IllegalArgumentException.class, () -> GeoConfig.builder().countryCode( "12" ).build() );
    }
}
