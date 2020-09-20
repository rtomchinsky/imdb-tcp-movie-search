package com.tomchinsky.imdb.service;

import com.tomchinsky.imdb.model.IMDBMovie;
import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import javax.inject.Inject;
import javax.inject.Named;
import java.io.IOException;
import java.util.Optional;
import java.util.stream.Stream;

public class IMDBSearchServiceImpl implements IMDBSearchService {
    private final String imdbUrl;
    private final IMDBMovieListParser scraper;
    private final OkHttpClient client = new OkHttpClient();

    @Inject
    public IMDBSearchServiceImpl(
            @Named("imdbUrl")
            final String imdbUrl,
            final IMDBMovieListParser scraper
    ) {
        this.imdbUrl = imdbUrl;
        this.scraper = scraper;
    }

    private Optional<String> makeRequest(final String query) throws IOException {
        final String lowered = query.toLowerCase();
        HttpUrl.Builder urlBuilder = HttpUrl.parse(imdbUrl).newBuilder()
                .addPathSegment(String.valueOf(lowered.charAt(0)))
                .addPathSegment(lowered + ".json");
        Request request = new Request.Builder()
                .url(urlBuilder.build())
                .build();

        try (Response response = client.newCall(request).execute()) {
            if (response.isSuccessful()) {
                return Optional.of(response.body().string());
            }
        }

        return Optional.empty();
    }

    @Override
    public Stream<IMDBMovie> findMovies(String query) throws IOException {
        return makeRequest(query)
                .map(s -> scraper.getMovieTitles(s))
                .map(stream -> stream.map(IMDBMovie::new))
                .orElseGet(Stream::empty);
    }
}
