package com.tomchinsky.imdb;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.text.MessageFormat;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicBoolean;

public class TCPServer {
    private final Executor executor = Executors.newCachedThreadPool();
    private final AtomicBoolean stop = new AtomicBoolean(false);

    private final ConnectionHandler connectionHandler;

    private volatile ServerSocket socket;

    public TCPServer(ConnectionHandler connectionHandler) {
        this.connectionHandler = connectionHandler;
    }

    public void start(int port) throws IOException {
        this.socket = new ServerSocket(port);
        System.out.println("Server listening for connections");

        while (!stop.get()) {
            final Socket client = socket.accept();
            System.out.println(MessageFormat.format("Got connection from {0}", client.getInetAddress()));
            executor.execute(() -> {
                try {
                    this.connectionHandler.run(client);
                } catch (Exception e) {
                    System.err.println(
                            MessageFormat.format(
                                    "Client ({}) connection handling failed: {0}",
                                    client.getInetAddress(),
                                    e.getMessage()
                            )
                    );
                } finally {
                    if (!client.isClosed()) {
                        try {
                            client.close();
                        } catch (IOException e) {
                            System.err.println("Failed to close client connection: " + e.getMessage());
                        }
                    }
                }
            });
        }
    }

    public String getHost() {
        return this.socket.getInetAddress().getHostAddress();
    }

    public int getPort() {
        return this.socket.getLocalPort();
    }

    public void stop() {
        this.stop.set(true);
    }
}
