package me.knighthat.extractor.asiancrush;

import me.knighthat.extractor.asiancrush.command.VideoImpl;
import me.knighthat.youtubedl.command.*;
import me.knighthat.youtubedl.response.format.MixFormat;
import org.jetbrains.annotations.NotNull;

/**
 * AsianCrush extractor
 */
public class AsianCrush {

    public static @NotNull Formats.Builder formats( @NotNull String url ) {
        return me.knighthat.extractor.asiancrush.command.FormatsImpl.builder( url );
    }

    public static @NotNull Stream.Builder stream( @NotNull String url ) {
        return StreamImpl.builder( url );
    }

    public static @NotNull Json.Builder json( @NotNull String url ) {
        return JsonImpl.builder( url );
    }

    public static @NotNull me.knighthat.extractor.asiancrush.command.Video.Builder video( @NotNull String url ) {
        return VideoImpl.builder( url );
    }

    public interface Movie extends MixFormat {
    }

    public interface Video extends me.knighthat.youtubedl.response.video.Video {
    }
}