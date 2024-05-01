package me.knighthat.youtubedl.command;

import org.jetbrains.annotations.NotNull;

public abstract class CommandTestTemplate {

    @NotNull
    static String URL = "https://youtube.com/watch?v=12312312312";

    abstract void testBuilder();
}
