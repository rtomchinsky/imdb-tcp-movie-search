package com.tomchinsky.imdb.service;

import com.tomchinsky.imdb.model.IMDBMovie;

import java.io.IOException;
import java.util.stream.Stream;

public interface IMDBSearchService {
    Stream<IMDBMovie> findMovies(String query) throws IOException;
}
