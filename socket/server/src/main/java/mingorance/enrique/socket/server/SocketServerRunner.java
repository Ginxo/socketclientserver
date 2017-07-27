package mingorance.enrique.socket.server;

import mingorance.cano.socket.commondomain.Message;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.CharBuffer;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class SocketServerRunner {

    private static SocketServerRunner instance;
    private int serverPort;
    private boolean running;

    private BufferedReader communicationFromClient;
    private PrintWriter communicationToClient;

    private MessagePool messagePool = new MessagePool();

    private SocketServerRunner() {
    }

    public static SocketServerRunner getInstance() {
        if (instance == null) {
            instance = new SocketServerRunner();
        }
        return instance;
    }


    public void run(final int serverPort) throws IOException, ServerException {
        if (this.running) {
            throw new ServerException(String.format("The server is already running in port %s", serverPort));
        }
        this.serverPort = serverPort;
        ServerSocket serverSocket = new ServerSocket(serverPort);
        serverSocket.setSoTimeout(30000);
        this.running = true;
        System.out.println("Listening for client on port " + serverSocket.getLocalPort() + "...");
        Socket server = serverSocket.accept();
        System.out.println("Just connected to " + server.getRemoteSocketAddress());
        this.communicationFromClient =
                new BufferedReader(
                        new InputStreamReader(server.getInputStream()));
        this.communicationToClient =
                new PrintWriter(server.getOutputStream(), true);

        this.receiveMessagesFromClient();
    }

    public Message getLastMessageFromPool() {
        Message result = this.messagePool.consumeMessage();
        if (result == null) {
            do {
                try {
                    Thread.sleep(2 * 1000);
                } catch (InterruptedException ex) {
                    Thread.currentThread().interrupt();
                }
            } while (!this.messagePool.hasMessages());
            return this.messagePool.consumeMessage();
        }
        return result;
    }

    public void sendToClient(final Message message) {
        //server.getLocalSocketAddress()
        communicationToClient.println(message);
    }


    private void receiveMessagesFromClient() throws IOException {
        System.out.println("Listening to client");
        Thread thread = new Thread() {
            public void run() {
                try {
                    while (running) {
                        final String messageFromClient = communicationFromClient.readLine();
                        System.out.println("Mensaje recibido");
                        CharBuffer cb = CharBuffer.allocate(messageFromClient.length());
                        cb.append(messageFromClient);
                        cb.rewind();
                        messagePool.read(cb);
                    }
                } catch (IOException e) {
                    System.err.println(e);
                }
            }
        };
        thread.start();
    }


}
