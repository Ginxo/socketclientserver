package mingorance.enrique.client;

import mingorance.cano.socket.commondomain.MessageType;

import java.io.IOException;
import java.util.Scanner;

/**
 * The type Server main.
 */
public class ClientMain {
    /**
     * The entry point of application.
     *
     * @param args the input arguments
     */
    public static void main(String[] args) throws IOException, ClientException {
        final SocketClientRunner socketClientRunner = SocketClientRunner.getInstance();
        socketClientRunner.run("localhost", 4200);

        String message;
        do {
            System.out.println("Insert a value to send to the server");
            Scanner in = new Scanner(System.in);
            message = in.nextLine();
            socketClientRunner.sendToServer(MessageType.MESSAGE, message);
        } while (!"exit".equalsIgnoreCase(message));
    }
}
