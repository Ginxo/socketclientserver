package mingorance.enrique.socket.server;

import mingorance.cano.socket.commondomain.Message;

import java.io.IOException;

/**
 * The type Server main.
 */
public class ServerMain {
    /**
     * The entry point of application.
     *
     * @param args the input arguments
     */
    public static void main(String[] args) throws IOException, ServerException {
        final SocketServerRunner socketServerRunner = SocketServerRunner.getInstance();
        socketServerRunner.run(4200);
        Message message;
        do {
            message = socketServerRunner.getLastMessageFromPool();
            System.out.println(String.format("Message received from client. Type: %s, Message: %s", message.getType()
                    , message.getMessage()));
        } while (!message.getMessage().equalsIgnoreCase("exit"));
        System.out.println("Exit received from client");

    }
}
