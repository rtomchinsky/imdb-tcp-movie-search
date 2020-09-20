package com.tomchinsky.imdb;

import org.junit.Assert;
import org.junit.Test;
import org.mockito.Mockito;

import java.io.IOException;
import java.net.Socket;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.Semaphore;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

import static org.junit.Assert.*;

public class TCPServerTest {

    @Test
    public void start() {
    }

    @Test
    public void acceptsMultipleClients() throws IOException, InterruptedException {
        // Given
        AtomicBoolean isFirstConnected = new AtomicBoolean(false);
        AtomicBoolean isSecondConnected = new AtomicBoolean(false);
        final ConnectionHandler handler = client -> {
            if (!isFirstConnected.compareAndSet(false, true)) {
                isSecondConnected.set(true);
            }
            while (true) {
                try {
                    Thread.sleep(1);
                } catch (InterruptedException e) {
                    return;
                }
            }
        };
        final TCPServer server = new TCPServer(handler);
        final CountDownLatch latch = new CountDownLatch(1);

        final Thread t = new Thread(() -> {
            try {
                latch.countDown();
                server.start(0);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
        t.start();
        latch.await();
        // allow some time for the values to be set
        Thread.sleep(10);
        final String host = server.getHost();
        final int port = server.getPort();

        // When
        new Socket(host, port);
        new Socket(host, port);

        // allow some time for the values to be set
        Thread.sleep(10);


        // Then
        Assert.assertTrue(isFirstConnected.get());
        Assert.assertTrue(isSecondConnected.get());

        server.stop();
        t.join(100);
    }

    @Test
    public void canBeStopped() throws InterruptedException {
        // Given
        final ConnectionHandler handler = Mockito.mock(ConnectionHandler.class);
        final TCPServer server = new TCPServer(handler);

        final Thread t = new Thread(() -> {
            try {
                server.start(0);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });

        // When
        t.start();

        // Then
        Assert.assertTrue(t.isAlive());

        // When
        server.stop();
        t.join(100);

        // Then
        Assert.assertFalse(t.isAlive());
    }

    @Test
    public void closesTheClientOnAfterItIsFinished() throws IOException, InterruptedException {
        // Given
        final ConnectionHandler handler = Mockito.mock(ConnectionHandler.class);

        final TCPServer server = new TCPServer(handler);
        final CountDownLatch latch = new CountDownLatch(1);

        final Thread t = new Thread(() -> {
            try {
                latch.countDown();
                server.start(0);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
        t.start();
        latch.await();
        // allow some time for the values to be set
        Thread.sleep(10);

        // When
        Socket client = new Socket(server.getHost(), server.getPort());

        // Then
        Assert.assertFalse(client.isClosed());

        server.stop();
        t.join(100);
    }

    @Test
    public void closesTheClientOnAfterItErrors() throws IOException, InterruptedException {
        // Given
        final ConnectionHandler handler = Mockito.mock(ConnectionHandler.class);
        Mockito.doThrow(new RuntimeException()).when(handler).run(Mockito.any());

        final TCPServer server = new TCPServer(handler);
        final CountDownLatch latch = new CountDownLatch(1);

        final Thread t = new Thread(() -> {
            try {
                latch.countDown();
                server.start(0);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
        t.start();
        latch.await();
        // allow some time for the values to be set
        Thread.sleep(10);

        // When
        Socket client = new Socket(server.getHost(), server.getPort());

        // Then
        Assert.assertFalse(client.isClosed());

        server.stop();
        t.join(100);
    }
}