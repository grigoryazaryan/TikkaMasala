package event_bus_event;

import model.Message;

/**
 * Created by grigory on 16/01/18.
 */

public class EventBusMessage {
    private final Message message;

    public EventBusMessage(Message message) {
        this.message = message;
    }

    public Message getMessage() {
        return message;
    }
}
