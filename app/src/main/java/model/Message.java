package model;

/**
 * Created by grigory on 16/01/18.
 */

/**
 * Just raw String message.
 * Can be modified to more complicated object
 */
public class Message {
    private String sender;
    private String message;

    public Message(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

    @Override
    public String toString() {
        return "message: " + message;
    }
}
