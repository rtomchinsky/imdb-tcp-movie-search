package com.tomchinsky.imdb.controller;

import com.tomchinsky.imdb.model.IMDBMovie;
import com.tomchinsky.imdb.service.IMDBSearchService;
import org.junit.Assert;
import org.junit.Test;
import org.mockito.Mockito;

import java.io.*;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.util.stream.Stream;

public class MovieTitleConnectionHandlerImplTest {
    @Test
    public void canGetAndSendData() throws IOException {
        // Given
        IMDBSearchService searchService = Mockito.mock(IMDBSearchService.class);
        MovieTitleConnectionHandler handler = new MovieTitleConnectionHandlerImpl(searchService);
        Socket client = Mockito.mock(Socket.class);

        ByteArrayOutputStream os = new ByteArrayOutputStream();
        InputStream is = new ByteArrayInputStream("3:abc".getBytes(StandardCharsets.UTF_8));

        Mockito.when(client.getOutputStream()).thenReturn(os);
        Mockito.when(client.getInputStream()).thenReturn(is);
        Mockito.when(searchService.findMovies("abc"))
                .thenReturn(Stream.of("abc1", "abc2").map(IMDBMovie::new));

        // When
        handler.run(client);
        String response = new String(os.toByteArray(), StandardCharsets.UTF_8);

        // Then
        Assert.assertEquals("10:abc1\nabc2\n", response);
    }
}