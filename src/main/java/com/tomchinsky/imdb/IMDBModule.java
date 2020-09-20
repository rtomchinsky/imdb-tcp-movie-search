package com.tomchinsky.imdb;

import com.google.inject.AbstractModule;
import com.google.inject.name.Names;
import com.tomchinsky.imdb.controller.MovieTitleConnectionHandler;
import com.tomchinsky.imdb.controller.MovieTitleConnectionHandlerImpl;
import com.tomchinsky.imdb.service.IMDBMovieListParser;
import com.tomchinsky.imdb.service.IMDBMovieListParserImpl;
import com.tomchinsky.imdb.service.IMDBSearchService;
import com.tomchinsky.imdb.service.IMDBSearchServiceImpl;

public class IMDBModule extends AbstractModule {
    @Override
    protected void configure() {
        bind(IMDBMovieListParser.class).to(IMDBMovieListParserImpl.class);
        bind(IMDBSearchService.class).to(IMDBSearchServiceImpl.class);
        bind(MovieTitleConnectionHandler.class).to(MovieTitleConnectionHandlerImpl.class);
        bindConstant().annotatedWith(Names.named("imdbUrl")).to("https://sg.media-imdb.com/suggests");
    }
}
