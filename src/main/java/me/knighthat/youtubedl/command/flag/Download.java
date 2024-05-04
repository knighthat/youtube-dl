package me.knighthat.youtubedl.command.flag;

import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;

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

        public @NotNull Builder rate(int bytes) {
            this.rate = bytes;
            return this;
        }

        public @NotNull Builder retries(int retries) {
            this.retries = retries;
            return this;
        }

        public @NotNull Download build() { return new Download(rate, retries); }
    }
}
