package me.knighthat.deps.command;

import me.knighthat.deps.response.ListResponse;
import me.knighthat.deps.response.format.AudioOnly;
import me.knighthat.deps.response.format.Format;
import me.knighthat.deps.response.format.Mix;
import me.knighthat.deps.response.format.VideoOnly;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class Formats extends YtdlCommand implements ListResponse {

    public Formats( @NotNull String url ) {
        super( url );
        flags( () -> Map.of( "-F", "" ) );
    }

    public @NotNull List<Format> execute() {
        List<Format> responseFormats = new ArrayList<>();
        List<String> outputs = new ArrayList<>();
        try {
            outputs = super.execute0();
        } catch ( IOException | InterruptedException ignored ) {
        }

        for (String output : outputs) {
            if ( !Character.isDigit( output.charAt( 0 ) ) )
                continue;

            String[] parts = output.trim().split( "," );
            String[] info = parts[0].trim().split( "\\s+" );

                /*
                    Video
                    [400, mp4, 2560x1440, 1440p, 5314k, mp4_dash container, av01.0.12M.08, 24fps, video only, 152.20MiB]

                    Audio
                    [140, m4a, audio, only, audio_quality_medium, 129k, m4a_dash container, mp4a.40.2 (44100Hz), 3.71MiB]

                    Mix
                    [18, mp4, 640x360, 360p, 595k, avc1.42001E, 24fps, mp4a.40.2 (44100Hz) (best)]
                */
            Format.Type type = Format.Type.MIX;

            String[] all = new String[parts.length + info.length - 1];
            for (int i = 0 ; i < info.length ; i++) {
                String part = info[i].trim();
                if ( part.equals( "audio" ) )
                    type = Format.Type.AUDIO_ONLY;
                all[i] = part;
            }
            for (int i = 1 ; i < parts.length ; i++) {
                String part = parts[i].trim();
                if ( part.equals( "video only" ) )
                    type = Format.Type.VIDEO_ONLY;
                all[i + info.length - 1] = part;
            }

            responseFormats.add(
                    switch (type) {
                        case VIDEO_ONLY -> new VideoOnly( all );
                        case AUDIO_ONLY -> new AudioOnly( all );
                        case MIX -> new Mix( all );
                    }
            );
        }

        return responseFormats;
    }
}
