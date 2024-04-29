package me.knighthat.youtubedl.command.flag;

import org.jetbrains.annotations.NotNull;

import java.util.Map;

public class FlagUtils {

    static String @NotNull [] mapToArray( @NotNull Map<String, String> map ) {
        String[] results = new String[map.size() * 2];

        int index = 0;
        for (Map.Entry<String, String> entry : map.entrySet()) {
            results[index++] = entry.getKey();
            results[index++] = entry.getValue();
        }

        return results;
    }
}
