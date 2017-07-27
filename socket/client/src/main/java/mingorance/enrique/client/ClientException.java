package mingorance.enrique.client;

/**
 * The type Client exception.
 */
public class ClientException extends Exception {
    /**
     * Instantiates a new Client exception.
     *
     * @param message the message
     */
    public ClientException(String message) {
        super(message);
    }

    /**
     * Instantiates a new Client exception.
     *
     * @param message the message
     * @param cause   the cause
     */
    public ClientException(String message, Throwable cause) {
        super(message, cause);
    }
}
