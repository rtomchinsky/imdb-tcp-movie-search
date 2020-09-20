package com.tomchinsky.imdb.model;

import org.jetbrains.annotations.NotNull;

public class IMDBMovie {
    @NotNull
    private final String title;

    public IMDBMovie(@NotNull final String title) {
        this.title = title;
    }

    @NotNull
    public String getTitle() {
        return title;
    }
}
