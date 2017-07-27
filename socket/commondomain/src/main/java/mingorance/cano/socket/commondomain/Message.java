package mingorance.cano.socket.commondomain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

/**
 * The type Message.
 */
@Getter
@Setter
public class Message {

    private MessageType type;
    private String message;
    private Date creationDate;

    public Message(final MessageType type, final String message) {
        this.type = type;
        this.message = message;
        this.creationDate = new Date();
    }

    @Override
    public String toString() {
        return String.format("%s#%s", this.type, this.message);
    }

    /**
     * From string message.
     *
     * @param message the message
     *
     * @return the message
     */
    public static Message fromString(final String message) {
        String[] messageSplit = message.split("#");
        if (messageSplit.length > 1) {
            final String type = messageSplit[0];
            final String textMessage = messageSplit[1];
            return new Message(MessageType.fromString(type), textMessage);
        }
        return null;
    }
}
