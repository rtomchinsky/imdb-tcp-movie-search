package com.tomchinsky.imdb.service;

import com.tomchinsky.imdb.model.IMDBMovie;
import org.junit.Assert;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.Mockito;
import org.mockserver.client.MockServerClient;
import org.mockserver.junit.MockServerRule;
import org.mockserver.model.HttpRequest;
import org.mockserver.model.HttpResponse;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class IMDBSearchServiceImplTest {
    @Rule
    public MockServerRule mockServerRule = new MockServerRule(this);

    @Test
    public void findMovies() throws IOException {
        // Given
        MockServerClient client = mockServerRule.getClient();
        IMDBMovieListParser parser = Mockito.mock(IMDBMovieListParser.class);
        Mockito.when(parser.getMovieTitles(Mockito.anyString())).thenReturn(Stream.of("a", "b"));

        final IMDBSearchServiceImpl impl = new IMDBSearchServiceImpl(
                "http://localhost:" + client.getPort(),
                parser
        );

        client.when(HttpRequest.request("/a/a%20movie.json"))
                .respond(HttpResponse.response("imdb$a_movie(...)"));

        // When
        List<String> movies = impl.findMovies("a movie")
                .map(IMDBMovie::getTitle)
                .collect(Collectors.toList());

        // Then
        Assert.assertEquals(movies, Arrays.asList("a", "b"));
    }
}