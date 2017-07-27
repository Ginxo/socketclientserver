package mingorance.enrique.client;

import mingorance.cano.socket.commondomain.Message;
import mingorance.cano.socket.commondomain.MessageType;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ConnectException;
import java.net.InetAddress;
import java.net.Socket;

/**
 * Created by emingora on 27/07/2017.
 */
public class SocketClientRunner {


    /**
     * The constant RETRY_TIMES.
     */
    public static final int RETRY_TIMES = 3;
    /**
     * The constant WAITING_TIME.
     */
    public static final int WAITING_TIME = 5;

    private static SocketClientRunner instance;
    private String serverIp;
    private int serverPort;
    private boolean running;
    private InetAddress host;

    private Socket socket;
    private BufferedReader communicationFromServer;
    private PrintWriter communicationToServer;

    private SocketClientRunner() {
    }

    /**
     * Gets instance.
     *
     * @return the instance
     */
    public static SocketClientRunner getInstance() {
        if (instance == null) {
            instance = new SocketClientRunner();
        }
        return instance;
    }

    /**
     * Run.
     *
     * @param ip         the serverIp
     * @param serverPort the server port
     *
     * @throws IOException     the io exception
     * @throws ClientException the client exception
     */
    public void run(final String ip, final int serverPort) throws IOException, ClientException {
        if (this.running) {
            throw new ClientException(String.format("The server is already running in port %s", serverPort));
        }
        this.serverIp = ip;
        this.serverPort = serverPort;
        this.host = InetAddress.getByName(ip);
        this.running = true;
        this.connect(1);
        System.out.println("Just connected to " + socket.getRemoteSocketAddress());
        this.communicationFromServer =
                new BufferedReader(
                        new InputStreamReader(socket.getInputStream()));
        this.communicationToServer =
                new PrintWriter(socket.getOutputStream(), true);
    }

    /**
     * Stop.
     *
     * @throws IOException the io exception
     */
    public void stop() throws IOException {
        this.communicationToServer.close();
        this.communicationFromServer.close();
        this.socket.close();
        this.running = false;
    }

    /**
     * Wait client string.
     *
     * @return the string
     *
     * @throws IOException the io exception
     */
    public Message waitServer() throws IOException {
        System.out.println("Waiting to server");
        return Message.fromString(communicationFromServer.readLine());
    }

    /**
     * Send to client.
     *
     * @param messageType the message type
     * @param message     the message
     */
    public void sendToServer(final MessageType messageType, final String message) {
        //server.getLocalSocketAddress()
        communicationToServer.println(new Message(messageType, message));
    }

    private void connect(int retryTimes) throws IOException, ClientException {
        System.out.println(String.format("Connecting to server [%s] on port [%s]. [%d of %d]", this
                .serverIp, this
                .serverPort, retryTimes, RETRY_TIMES));
        try {
            this.socket = new Socket(this.host, this.serverPort);

        } catch (ConnectException e) {
            System.err.println(String.format("Error trying to connect with the server. %d of %d times",
                    retryTimes, RETRY_TIMES));

            if (retryTimes >= RETRY_TIMES) {
                throw new ClientException(String.format("Error after trying %d", retryTimes), e);
            }

            System.err.println(String.format("Let´s try it again after %d seconds...",
                    WAITING_TIME));
            try {
                Thread.sleep(WAITING_TIME * 1000);
            } catch (InterruptedException ex) {
                Thread.currentThread().interrupt();
            }
            this.connect(++retryTimes);
        }
    }


}