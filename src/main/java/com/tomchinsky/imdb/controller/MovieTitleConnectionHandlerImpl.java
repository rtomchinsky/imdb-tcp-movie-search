
package com.tomchinsky.imdb.controller;

import com.tomchinsky.imdb.model.IMDBMovie;
import com.tomchinsky.imdb.service.IMDBSearchService;

import javax.inject.Inject;
import java.io.*;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class MovieTitleConnectionHandlerImpl implements MovieTitleConnectionHandler {
    private final IMDBSearchService searchService;

    @Inject
    public MovieTitleConnectionHandlerImpl(IMDBSearchService searchService) {
        this.searchService = searchService;
    }

    @Override
    public void run(Socket client) throws IOException {
        try (
                BufferedReader reader = new BufferedReader(
                        new InputStreamReader(client.getInputStream(), StandardCharsets.UTF_8)
                );
                BufferedWriter writer = new BufferedWriter(
                        new OutputStreamWriter(client.getOutputStream(), StandardCharsets.UTF_8)
                )
        ) {
            final ProtocolReader protocolReader = new ProtocolReader(reader);
            final ProtocolWriter protocolWriter = new ProtocolWriter(writer);
            while (true) {
                Optional<String> next = protocolReader.getMessage();
                if (!next.isPresent()) {
                    break;
                }

                String message = next.get().trim();

                System.out.println("Received message '" + message + "'");
                List<String> movies = searchService.findMovies(message)
                        .map(IMDBMovie::getTitle)
                        .collect(Collectors.toList());

                protocolWriter.write(movies);
            }
        } catch (Exception e) {
            System.err.println("Connection failed for client: " + e.getMessage());
        }
    }
}
