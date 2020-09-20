package com.tomchinsky.imdb.controller;

import org.junit.Test;
import org.mockito.Mockito;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

import java.io.EOFException;
import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;
import java.nio.CharBuffer;
import java.util.Optional;

import static org.junit.Assert.*;

public class ProtocolReaderTest {
    @Test
    public void testCanGetAMessageFromTheReader() throws IOException {
        // Given
        ProtocolReader reader = new ProtocolReader(new StringReader("10:abcdefghij"));

        // When
        Optional<String> next = reader.getMessage();

        // Then
        assertTrue(next.isPresent());
        assertEquals("abcdefghij", next.get());
    }

    @Test
    public void testCanGetMultipleMessagedFromReader() throws IOException {
        // Given
        ProtocolReader reader = new ProtocolReader(new StringReader("10:abcdefghij10:jihgfedcba"));

        // When
        Optional<String> first = reader.getMessage();
        Optional<String> second = reader.getMessage();

        // Then
        assertTrue(first.isPresent());
        assertEquals("abcdefghij", first.get());
        assertTrue(second.isPresent());
        assertEquals("jihgfedcba", second.get());
    }

    @Test
    public void testReturnsEmptyWhenReaderEnds() throws IOException {
        // Given
        ProtocolReader reader = new ProtocolReader(new StringReader("10:abcdefghij"));

        // When
        Optional<String> first = reader.getMessage();
        Optional<String> second = reader.getMessage();

        // Then
        assertTrue(first.isPresent());
        assertEquals("abcdefghij", first.get());
        assertFalse(second.isPresent());
    }

    @Test
    public void testThrowsWhenGetLengthMethodFails() throws IOException {
        Reader inner = Mockito.mock(Reader.class);
        Mockito.when(inner.read()).thenThrow(new IOException("EXPECTED"));

        ProtocolReader reader = new ProtocolReader(inner);

        try {
            reader.getMessage();
            fail("Should have thrown exception");
        } catch (IOException e) {
            assertEquals("EXPECTED", e.getCause().getMessage());
        }
    }

    @Test
    public void testThrowsWhenGetMessageMethodFails() throws IOException {
        Reader inner = Mockito.mock(Reader.class);
        Mockito.when(inner.read()).thenAnswer(new Answer<Integer>() {
            private int count = 0;
            @Override
            public Integer answer(InvocationOnMock invocation) throws Throwable {
                int current = count++;
                if (current == 0) {
                    return (int)'1';
                } else if (current == 1) {
                    return (int)':';
                } else {
                    throw new IOException("UNEXPECTED");
                }
            }
        });
        Mockito.when(inner.read(Mockito.any(CharBuffer.class))).thenThrow(new IOException("EXPECTED"));
        ProtocolReader reader = new ProtocolReader(inner);

        try {
            reader.getMessage();
            fail("Should have thrown exception");
        } catch (IOException e) {
            assertEquals("EXPECTED", e.getCause().getMessage());
        }
    }

    @Test(expected = EOFException.class)
    public void testThrowsWhenReachesEOFEarly() throws IOException {
        ProtocolReader reader = new ProtocolReader(new StringReader("10:abcde"));
        reader.getMessage();
    }
}