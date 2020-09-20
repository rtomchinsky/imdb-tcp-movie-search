package com.tomchinsky.imdb.service;

import java.util.stream.Stream;

public interface IMDBMovieListParser {
    Stream<String> getMovieTitles(String json);
}
