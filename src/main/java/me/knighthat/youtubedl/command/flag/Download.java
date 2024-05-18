package me.knighthat.youtubedl.command.flag;

import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;

/**
 * A set of configurations that allows user to
 * modify how youtube-dl handles received data.
 * <p>
 * For example, limit the download rate and
 * how many time to retry the download if failure
 * happens while running.
 */
public class Download implements Flag {

    public static @NotNull Builder builder() { return new Builder(); }

    private final int rate;
    private final int retries;

    private Download(int rate, int reties) {
        this.rate = rate;
        this.retries = reties;
    }

    public String @NotNull [] flags() {
        Map<String, String> results = new HashMap<>();

        if (rate > 0)
            results.put("--limit-rate", String.valueOf(rate));
        if (retries > 0)
            results.put("--retries", String.valueOf(retries));

        return FlagUtils.mapToArray(results);
    }

    public static class Builder {
        private int rate;
        private int retries;

        private Builder() { }

        /**
         * How many bytes should youtube-dl download data
         * from server per second.
         * <p>
         * Note: This method overrides the value 
         * of the same method called previously.
         * 
         * @param bytes how big a chunk of data is downloaded per second
         * 
         * @return same builder instance with updated value
         */
        public @NotNull Builder rate(int bytes) {
            this.rate = bytes;
            return this;
        }

        /**
         * How many times should youtube-dl attempt to 
         * resume the download when it fails.
         * <p>
         * Note: This method overrides the value 
         * of the same method called previously.
         * 
         * @param retries number of times to resume download
         *
         * @return same builder instance with updated value
         */
        public @NotNull Builder retries(int retries) {
            this.retries = retries;
            return this;
        }

        /**
         * Finalize the configuration and pack it in {@link me.knighthat.youtubedl.command.flag.Download} class.
         * 
         * @return finalized {@link me.knighthat.youtubedl.command.flag.Download} class 
         */
        public @NotNull Download build() { return new Download(rate, retries); }
    }
}
