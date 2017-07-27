package mingorance.cano.socket.commondomain;

import java.util.stream.Stream;

/**
 * The enum Message type.
 */
public enum MessageType {
    /**
     * Message message type.
     */
    MESSAGE,
    /**
     * Action message type.
     */
    ACTION;

    /**
     * From string message type.
     *
     * @param messageType the message type
     *
     * @return the message type
     */
    public static MessageType fromString(final String messageType) {
        return Stream.of(MessageType.values()).filter(MT -> MT.name().equalsIgnoreCase(messageType)).findFirst().orElse
                (null);
    }
}
