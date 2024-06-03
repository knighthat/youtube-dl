package me.knighthat.extractor.youtube.response.thumbnail;

import org.jetbrains.annotations.NotNull;

import com.google.gson.JsonObject;

import me.knighthat.extractor.youtube.YouTube;

public record Thumbnail(
    @NotNull String url,
    int width,
    int height
) implements YouTube.Thumbnail {

    public Thumbnail( @NotNull JsonObject json ) {
        this(
            json.get( "url" ).getAsString(), 
            json.get( "width" ).getAsInt(), 
            json.get( "height" ).getAsInt()
        );
    }
}