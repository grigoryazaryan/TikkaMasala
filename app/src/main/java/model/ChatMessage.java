package model;

import java.io.Serializable;

/**
 * Created by grigory on 17/01/18.
 */

public class ChatMessage implements Serializable {
    public static final int TYPE_TEXT = 1, TYPE_AUDIO_REC = 2;

    String sender;
    int type;
    long time;

    public ChatMessage(){
    }

    public String getSender() {
        return sender;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }

    @Override
    public String toString() {
        return "time: " + time
                + " sender: " + sender
                + " type: " + type;
    }
}
