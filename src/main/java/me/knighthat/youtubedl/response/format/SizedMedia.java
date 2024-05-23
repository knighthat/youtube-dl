package me.knighthat.youtubedl.response.format;

import java.math.BigInteger;

import org.jetbrains.annotations.NotNull;

/**
 * SizedMedia
 */
public interface SizedMedia extends Format{

    @NotNull BigInteger size();
}