package me.knighthat.youtubedl.command.flag;

import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.Locale;

public class GeoConfig implements Flag {

    public static @NotNull Builder builder() { return new Builder(); }

    private final boolean bypass;
    @NotNull
    private final String  countryCode;

    private GeoConfig( boolean bypass, @NotNull String countryCode ) {
        this.bypass = bypass;
        this.countryCode = countryCode;
    }

    @Override
    public String @NotNull [] flags() {
        // countryCode is never empty
        int size = (bypass ? 2 : 1) * 2;
        String[] results = new String[size];

        int index = 0;
        if ( bypass ) {
            results[index++] = "--geo-bypass";
            results[index++] = "";
        }
        results[index++] = "--geo-bypass-country";
        results[index++] = countryCode;

        return results;
    }

    public static class Builder {
        private static final String @NotNull [] isoCodes = Locale.getISOCountries();

        private boolean bypass      = false;
        private String  countryCode = Locale.getDefault().getCountry();

        private Builder() { }

        public @NotNull Builder bypass() {
            bypass = true;
            return this;
        }

        public @NotNull Builder countryCode( @NotNull String countryCode ) {
            int index = Arrays.binarySearch( isoCodes, countryCode );
            if ( index == -1 )
                throw new IllegalArgumentException( "Invalid country code: " + countryCode );
            else
                this.countryCode = countryCode;

            return this;
        }

        public @NotNull GeoConfig build() { return new GeoConfig( bypass, countryCode ); }
    }
}
