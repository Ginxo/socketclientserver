package mingorance.enrique.socket.server;

import mingorance.cano.socket.commondomain.Message;

import java.io.IOException;
import java.nio.CharBuffer;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * The type Message pool.
 */
class MessagePool implements Readable {
    private List<Message> messages = new ArrayList<>();

    @Override
    public int read(CharBuffer cb) throws IOException {
        final String messageString = cb.toString();
        if (cb.get() != '\u0000') {
            final Message message = Message.fromString(messageString);
            if (message != null) {
                messages.add(message);
                return 1;
            }
        }
        return 0;
    }

    /**
     * Consume message message.
     *
     * @return the message
     */
    public Message consumeMessage() {
        Message result = null;
        if (this.hasMessages()) {
            result = this.messages.get(0);
            this.messages.remove(result);
        }
        return result;
    }

    /**
     * Has messages boolean.
     *
     * @return the boolean
     */
    public boolean hasMessages() {
        return this.messages.size() > 0;
    }
}
