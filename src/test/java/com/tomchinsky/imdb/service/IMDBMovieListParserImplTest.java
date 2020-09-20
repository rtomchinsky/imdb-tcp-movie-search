package com.tomchinsky.imdb.service;

import org.junit.Assert;
import org.junit.Test;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.List;
import java.util.stream.Collectors;

public class IMDBMovieListParserImplTest {
    @Test
    public void testGetMovieTitles() {
        // Given
        InputStream jsonpResponse = getClass().getResourceAsStream("/lotr.jsonp");
        BufferedReader reader = new BufferedReader(new InputStreamReader(jsonpResponse));
        String jsonp = reader.lines().collect(Collectors.joining());
        IMDBMovieListParserImpl impl = new IMDBMovieListParserImpl();

        // When
        List<String> result = impl.getMovieTitles(jsonp).collect(Collectors.toList());

        // Then
        Assert.assertEquals(4, result.size());
    }
}