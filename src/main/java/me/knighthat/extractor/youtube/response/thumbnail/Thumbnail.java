package me.knighthat.extractor.youtube.response.thumbnail;


/**
 * Contains more details about a thumbnail, such as width and height.
 */
public interface Thumbnail extends me.knighthat.youtubedl.response.thumbnail.Thumbnail {

    int width();

    int height();
}