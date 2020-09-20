package com.tomchinsky.imdb.controller;

import org.junit.Assert;
import org.junit.Test;
import org.mockito.Mockito;

import java.io.IOException;
import java.io.StringWriter;
import java.io.Writer;
import java.util.Arrays;

public class ProtocolWriterTest {
    @Test
    public void testWritesIterableCorrectly() throws IOException {
        // Given
        StringWriter inner = new StringWriter();
        ProtocolWriter writer = new ProtocolWriter(inner);
        Iterable<String> strings = Arrays.asList("lord of the rings", "another movie");

        // When
        writer.write(strings);

        // Then
        Assert.assertEquals("32:lord of the rings\nanother movie\n", inner.toString());
    }

    @Test
    public void testWritesVarargsCorrectly() throws IOException {
        // Given
        StringWriter inner = new StringWriter();
        ProtocolWriter writer = new ProtocolWriter(inner);

        // When

        writer.write("lord of the rings", "another movie");

        // Then
        Assert.assertEquals("32:lord of the rings\nanother movie\n", inner.toString());
    }

    @Test
    public void testThrowsIOExceptionOnFailure() throws IOException {
        // Given
        Writer inner = Mockito.mock(Writer.class);
        Mockito.doThrow(new IOException("EXPECTED")).when(inner).write(Mockito.anyString());
        ProtocolWriter writer = new ProtocolWriter(inner);

        try {
            // When
            writer.write("lord of the rings", "another movie");
            Assert.fail("Should have thrown IOException");
        } catch (IOException e) {
            // Then
            Assert.assertEquals("EXPECTED", e.getCause().getMessage());
        }
    }
}
