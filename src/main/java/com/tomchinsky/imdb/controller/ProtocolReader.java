package com.tomchinsky.imdb.controller;

import org.jetbrains.annotations.NotNull;

import java.io.EOFException;
import java.io.IOException;
import java.io.Reader;
import java.nio.CharBuffer;
import java.util.Optional;

public class ProtocolReader {
    @NotNull
    private final Reader reader;
    private boolean open = true;

    public ProtocolReader(@NotNull Reader reader) {
        this.reader = reader;
    }

    public Optional<String> getMessage() throws IOException {
        int length = readLength();
        if (length == 0) {
            return Optional.empty();
        }
        return Optional.of(readMessage(length));
    }

    private int readLength() throws IOException {
        StringBuilder builder = new StringBuilder();
        int next;
        try {
            while ((next = reader.read()) != -1) {
                char nextChar = (char) next;
                if (nextChar == ':') {
                    return Integer.parseInt(builder.toString());
                }
                builder.append(nextChar);
            }
        } catch (IOException e) {
            throw new IOException("Failed to read message length from input", e);
        }

        return 0;
    }

    private String readMessage(int length) throws IOException {
        final CharBuffer buffer = CharBuffer.allocate(length);
        try {
            int totalRead = 0;
            int read;
            while ((read = reader.read(buffer)) != -1) {
                totalRead += read;
                if (totalRead == length) {
                    return buffer.rewind().toString();
                }
            }
        } catch (IOException e) {
            throw new IOException("Failed to read message from input", e);
        }
        throw new EOFException("Could not read message to the end");
    }
}
