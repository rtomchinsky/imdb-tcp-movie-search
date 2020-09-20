package com.tomchinsky.imdb.controller;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.io.Writer;
import java.util.ArrayList;
import java.util.Arrays;

public class ProtocolWriter {
    @NotNull
    private final Writer writer;

    public ProtocolWriter(@NotNull Writer writer) {
        this.writer = writer;
    }

    public void write(Iterable<String> strings) throws IOException {
        try {
            StringBuilder builder = new StringBuilder();
            for (String s : strings) {
                builder.append(s);
                builder.append("\n");
            }

            synchronized (writer) {
                writer.write(String.valueOf(builder.length()));
                writer.write(':');
                writer.write(builder.toString());
                writer.flush();
            }
        } catch (IOException e) {
            throw new IOException("Failed to write the protocol message", e);
        }
    }

    public void write(String... strings) throws IOException {
        write(Arrays.asList(strings));
    }
}
