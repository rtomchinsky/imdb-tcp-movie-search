package com.tomchinsky.imdb;

import com.tomchinsky.imdb.controller.ProtocolReader;
import com.tomchinsky.imdb.controller.ProtocolWriter;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.InetAddress;
import java.net.Socket;
import java.util.Optional;
import java.util.Scanner;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Client {

    public static void main(String[] args) throws IOException {
        ExecutorService executor = Executors.newSingleThreadExecutor();
        Socket socket = new Socket(InetAddress.getLocalHost(), 8000);

        ProtocolReader reader = new ProtocolReader(new InputStreamReader(socket.getInputStream()));
        ProtocolWriter writer = new ProtocolWriter(new BufferedWriter(new OutputStreamWriter(socket.getOutputStream())));
        executor.execute(() -> {
            try {
                while (true) {
                    Optional<String> message = reader.getMessage();
                    if (!message.isPresent()) {
                        break;
                    }

                    System.out.print(message.get());
                }
            } catch (Exception ignored) {
            }
        });

        Scanner scanner = new Scanner(System.in);

        System.out.println("Write \\quit any time to exit");

        while (true) {
            final String query = scanner.nextLine().trim();
            if ("\\quit".equals(query)) {
                break;
            } else if (query.length() > 0) {
                writer.write(query);
            }
        }

        socket.close();
        executor.shutdown();
    }
}
