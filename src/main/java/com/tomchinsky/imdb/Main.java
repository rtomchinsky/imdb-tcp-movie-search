package com.tomchinsky.imdb;

import com.google.inject.Guice;
import com.google.inject.Injector;
import com.tomchinsky.imdb.controller.MovieTitleConnectionHandler;

import java.io.IOException;

public class Main {
    public static void main(String[] args) {
        try {
            Injector injector = Guice.createInjector(new IMDBModule());
            TCPServer server = new TCPServer(injector.getInstance(MovieTitleConnectionHandler.class));

            Runtime.getRuntime().addShutdownHook(new Thread(() -> {
                System.out.println("Shutting down");
                server.stop();
            }));

            server.start(8000);
        } catch (IOException e) {
            System.err.println("Server failed with exception " + e.getMessage());
        }
    }
}
