package model;

/**
 * Created by grigory on 16/01/18.
 */

public class TextMessage extends ChatMessage {

    String message;

    public TextMessage() {
        this.type = TYPE_TEXT;
    }

    public TextMessage(String message) {
        this();
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    @Override
    public String toString() {
        return super.toString() + " message: " + message;
    }
}
