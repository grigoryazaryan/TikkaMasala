package model;

/**
 * Created by grigory on 16/01/18.
 */

/**
 * Just raw String message.
 * Can be modified to more complicated object
 */
public class Message {
    public String sender;
    public String message;

    public Message(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

    @Override
    public String toString() {
        return "sender: " + sender
                + " message: " + message;
    }
}
