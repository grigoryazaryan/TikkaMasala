package event_bus_event;

import model.TextMessage;

/**
 * Created by grigory on 16/01/18.
 */

public class EventBusMessage {
    private final TextMessage textMessage;

    public EventBusMessage(TextMessage textMessage) {
        this.textMessage = textMessage;
    }

    public TextMessage getTextMessage() {
        return textMessage;
    }
}
