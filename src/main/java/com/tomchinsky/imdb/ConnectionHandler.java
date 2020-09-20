package com.tomchinsky.imdb;

import java.io.IOException;
import java.net.Socket;

public interface ConnectionHandler {
    void run(Socket client) throws IOException;
}
