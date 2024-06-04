package me.knighthat.youtubedl.command.flag;

import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.Locale;

/**
 * A set of configurations that allows user to
 * fool server about client's geographical origin.
 * <p>
 * For example, it can be used to bypass region-lock
 * for some websites.
 */
public class GeoConfig implements Flag {

    public static @NotNull Builder builder() { return new Builder(); }

    private final boolean bypass;
    @NotNull
    private final String  countryCode;

    private GeoConfig( boolean bypass, @NotNull String countryCode ) {
        this.bypass = bypass;
        this.countryCode = countryCode;
    }

    /**
     * An array represents the geographical configurations
     * that will be sent along with the request.
     *
     * @return an array of arguments following [key, value] format
     */
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

        /**
         * Include a fake X-Forwarded-For inside the header
         * to fool the server about its origin.
         *
         * @return same builder instance with updated value
         */
        public @NotNull Builder bypass() {
            bypass = true;
            return this;
        }

        /**
         * Bypass region-lock with a different country code.
         * <p>
         * Country code must follow ISO 3166 - 2 letters country code.
         * Read more: <a href="https://www.iso.org/iso-3166-country-codes.html">ISO-3166</a>
         *
         * @param countryCode unique code of a country that follows ISO 3166 format
         *
         * @return same builder instance with updated value
         */
        public @NotNull Builder countryCode( @NotNull String countryCode ) {
            int index = Arrays.binarySearch( isoCodes, countryCode );
            if ( index == -1 )
                throw new IllegalArgumentException( "Invalid country code: " + countryCode );
            else
                this.countryCode = countryCode;

            return this;
        }

        /**
         * Finalize the configuration and pack it in {@link me.knighthat.youtubedl.command.flag.GeoConfig} class.
         *
         * @return finalized {@link me.knighthat.youtubedl.command.flag.GeoConfig} class
         */
        public @NotNull GeoConfig build() { return new GeoConfig( bypass, countryCode ); }
    }
}
