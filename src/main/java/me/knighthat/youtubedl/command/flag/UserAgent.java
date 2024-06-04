package me.knighthat.youtubedl.command.flag;

import org.jetbrains.annotations.NotNull;

public class UserAgent implements Flag {

    /*
        Chrome
    */

    /**
     * Most used Chrome browser on Windows.
     * <p>
     * Updated occasionally.
     */
    @NotNull
    public static final UserAgent CHROME_WINDOWS = new UserAgent( "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/123.0.0.0 Safari/537.3" );
    /**
     * Most used Chrome browser on macOS.
     * <p>
     * Updated occasionally.
     */
    @NotNull
    public static final UserAgent CHROME_MACOS   = new UserAgent( "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_15_7) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/119.0.0.0 Safari/537.3" );
    /**
     * Most used Chrome browser on Android.
     * <p>
     * Updated occasionally.
     */
    @NotNull
    public static final UserAgent CHROME_ANDROID = new UserAgent( "Mozilla/5.0 (Linux; Android 10; K) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/123.0.0.0 Mobile Safari/537.3" );
    /**
     * Most used Chrome browser on Linux.
     * <p>
     * Updated occasionally.
     */
    @NotNull
    public static final UserAgent CHROME_LINUX   = new UserAgent( "Mozilla/5.0 (X11; Linux x86_64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/124.0.0.0 Safari/537.36" );


    /*
        Firefox
    */

    /**
     * Most used firefox browser on Windows.
     * <p>
     * Updated occasionally.
     */
    @NotNull
    public static final UserAgent FIREFOX_WINDOWS = new UserAgent( "Mozilla/5.0 (Windows NT 10.0; Win64; x64; rv:125.0) Gecko/20100101 Firefox/125.0" );
    /**
     * Most used firefox browser on macOS.
     * <p>
     * Updated occasionally.
     */
    @NotNull
    public static final UserAgent FIREFOX_MACOS   = new UserAgent( "Mozilla/5.0 (Macintosh; Intel Mac OS X 14.4; rv:125.0) Gecko/20100101 Firefox/125.0" );
    /**
     * Most used firefox browser on Linux.
     * <p>
     * Updated occasionally.
     */
    @NotNull
    public static final UserAgent FIREFOX_LINUX   = new UserAgent( "Mozilla/5.0 (X11; Linux i686; rv:125.0) Gecko/20100101 Firefox/125.0" );
    /**
     * Most used firefox browser on Android.
     * <p>
     * Updated occasionally.
     */
    @NotNull
    public static final UserAgent FIREFOX_ANDROID = new UserAgent( "Mozilla/5.0 (Android 14; Mobile; rv:125.0) Gecko/125.0 Firefox/125.0" );


    /*
        Safari
    */

    /**
     * Most used safari browser.
     * <p>
     * Updated occasionally.
     */
    @NotNull
    public static final UserAgent SAFARI_IOS = new UserAgent( "Mozilla/5.0 (iPhone; CPU iPhone OS 17_4_1 like Mac OS X) AppleWebKit/605.1.15 (KHTML, like Gecko) Version/17.4.1 Mobile/15E148 " +
                                                              "Safari/604." );

    /**
     * Create a custom user agent in case default versions
     * aren't what user after.
     *
     * @param userAgent user agent string
     *
     * @return an instance of {@link me.knighthat.youtubedl.command.flag.UserAgent} represents input user agent string.
     */
    public static @NotNull UserAgent custom( @NotNull String userAgent ) { return new UserAgent( userAgent ); }

    @NotNull
    private final String userAgent;

    private UserAgent( @NotNull String userAgent ) { this.userAgent = userAgent; }

    /**
     * An array represents the user agent
     * that will be sent along with the request.
     *
     * @return an array of user agent tag and user agent string
     */
    @Override
    public String @NotNull [] flags() { return new String[]{ "--user-agent", userAgent }; }

    @Override
    public final String toString() { return userAgent; }
}
